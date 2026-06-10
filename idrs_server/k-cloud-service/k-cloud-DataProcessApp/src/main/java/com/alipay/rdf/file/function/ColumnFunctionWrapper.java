//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.alipay.rdf.file.function;

import com.alipay.rdf.file.exception.RdfErrorEnum;
import com.alipay.rdf.file.exception.RdfFileException;
import com.alipay.rdf.file.loader.ExtensionLoader;
import com.alipay.rdf.file.loader.FormatLoader;
import com.alipay.rdf.file.meta.FileColumnMeta;
import com.alipay.rdf.file.meta.FileMeta;
import com.alipay.rdf.file.model.FileConfig;
import com.alipay.rdf.file.model.FileDataTypeEnum;
import com.alipay.rdf.file.protocol.ColumnLayoutEnum;
import com.alipay.rdf.file.protocol.RowDefinition;
import com.alipay.rdf.file.spi.RdfFileColumnTypeSpi;
import com.alipay.rdf.file.spi.RdfFileFormatSpi;
import com.alipay.rdf.file.spi.RdfFileFunctionSpi;
import com.alipay.rdf.file.util.RdfFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ColumnFunctionWrapper extends RdfFunction {
    public static final Map<String, ColumnRegEx> columnRegExs = new ConcurrentHashMap();
    private List<RdfFileFunctionSpi> rdfFunctions;

    public ColumnFunctionWrapper(List<RdfFileFunctionSpi> rdfFunctions, FileDataTypeEnum rowType) {
        this.rdfFunctions = rdfFunctions;
        this.rowType = rowType;
    }

    public int rowsAffected(RowDefinition rd, FileMeta fileMeta) {
        if (!fileMeta.hasColumns(this.rowType)) {
            return 0;
        } else if (ColumnLayoutEnum.horizontal.equals(rd.getColumnLayout())) {
            return 1;
        } else if (ColumnLayoutEnum.vertical.equals(rd.getColumnLayout())) {
            return fileMeta.getColumns(this.rowType).size();
        } else {
            throw new RdfFileException("ColumnFunctionWrapper对column的布局[" + rd.getColumnLayout() + "] 无法计算rowsAffected", RdfErrorEnum.FUNCTION_ERROR);
        }
    }

    public Object execute(RdfFileFunctionSpi.FuncContext ctx) {
        if (CodecType.SERIALIZE.equals(ctx.codecType)) {
            return this.serialize(ctx.field, ctx.columnMeta, ctx.fileConfig);
        } else {
            return CodecType.DESERIALIZE.equals(ctx.codecType) ? this.deserialize((String)ctx.field, ctx.columnMeta, ctx.fileConfig) : null;
        }
    }

    private Object deserialize(String field, FileColumnMeta columnMeta, FileConfig fileConfig) {
        ColumnRegEx columnRegEx = this.getColumnRegEx(columnMeta);
        RdfFileFormatSpi columnFormat = FormatLoader.getColumnFormt(columnMeta.getFileMeta().getProtocol(), columnMeta.getType().getName());
        RdfFileUtil.assertNotNull(columnFormat, "rdf-file# protocol=" + columnMeta.getFileMeta().getProtocol() + " columnType=" + columnMeta.getType().getName() + " 没有获取到format", RdfErrorEnum.EXTENSION_ERROR);
        field = columnFormat.deserialize(field, columnMeta, fileConfig);
        if (columnRegEx.isRegx) {
            Pattern pattern = Pattern.compile(columnRegEx.regEx);
            Matcher matcher = pattern.matcher(field);
            /*if (!matcher.find()) {
                throw new RdfFileException("字段内容[" + field + "]无法通过正则[" + columnRegEx.regEx + "]匹配", RdfErrorEnum.DESERIALIZE_ERROR);
            }

            field = matcher.group(1);*/

            if (matcher.find()) {
                field = matcher.group(1);
            } else{
                log.info("字段内容[" + field + "]无法通过正则[" + columnRegEx.regEx + "]匹配");
            }

        } else {
            field = columnRegEx.regEx;
        }

        RdfFileColumnTypeSpi columnTypeCodec = (RdfFileColumnTypeSpi)ExtensionLoader.getExtensionLoader(RdfFileColumnTypeSpi.class).getExtension(columnMeta.getType().getName());
        RdfFileUtil.assertNotNull(columnTypeCodec, "没有type=" + columnMeta.getType().getName() + " 对应的类型codec");
        return columnTypeCodec.deserialize(field, columnMeta);
    }

    private String serialize(Object field, FileColumnMeta columnMeta, FileConfig fileConfig) {
        RdfFileColumnTypeSpi columnTypeCodec = (RdfFileColumnTypeSpi)ExtensionLoader.getExtensionLoader(RdfFileColumnTypeSpi.class).getExtension(columnMeta.getType().getName());
        RdfFileUtil.assertNotNull(columnTypeCodec, "没有type=" + columnMeta.getType().getName() + " 对应的类型codec");
        String value = columnTypeCodec.serialize(field, columnMeta);
        StringBuffer sb = new StringBuffer();
        Iterator var7 = this.rdfFunctions.iterator();

        while(var7.hasNext()) {
            RdfFileFunctionSpi rf = (RdfFileFunctionSpi)var7.next();
            RdfFileFunctionSpi.FuncContext ctx = new RdfFileFunctionSpi.FuncContext();
            ctx.field = value;
            ctx.columnMeta = columnMeta;
            ctx.codecType = CodecType.SERIALIZE;
            sb.append(rf.execute(ctx));
        }

        value = sb.toString();
        RdfFileFormatSpi columnFormat = FormatLoader.getColumnFormt(columnMeta.getFileMeta().getProtocol(), columnMeta.getType().getName());
        return columnFormat.serialize(value, columnMeta, fileConfig);
    }

    private ColumnRegEx getColumnRegEx(FileColumnMeta columnMeta) {
        String key = columnMeta.getFileMeta().getTemplatePath() + "-" + columnMeta.getDataType().name() + "-" + columnMeta.getName();
        ColumnRegEx columnRegEx = (ColumnRegEx)columnRegExs.get(key);
        if (null == columnRegEx) {
            columnRegEx = new ColumnRegEx();
            if (this.rdfFunctions.size() != 0) {
                StringBuffer sb = new StringBuffer();
                Iterator var5 = this.rdfFunctions.iterator();

                while(var5.hasNext()) {
                    RdfFileFunctionSpi rf = (RdfFileFunctionSpi)var5.next();
                    if ("value".equals(rf.getExpression())) {
                        columnRegEx.isRegx = true;
                        sb.append("(.*)");
                    } else {
                        RdfFileFunctionSpi.FuncContext ctx = new RdfFileFunctionSpi.FuncContext();
                        ctx.columnMeta = columnMeta;
                        ctx.codecType = CodecType.DESERIALIZE;
                        Object ret = rf.execute(ctx);
                        sb.append(ret);
                    }
                }

                if (columnRegEx.isRegx) {
                    sb.insert(0, "^");
                    sb.append("$");
                }

                columnRegEx.regEx = sb.toString();
            }

            columnRegExs.put(key, columnRegEx);
        }

        return columnRegEx;
    }

    private static class ColumnRegEx {
        boolean isRegx;
        String regEx;

        private ColumnRegEx() {
        }
    }
}
