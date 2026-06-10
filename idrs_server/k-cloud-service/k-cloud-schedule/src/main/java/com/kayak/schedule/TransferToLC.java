package com.kayak.schedule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.util.FileUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import com.google.common.io.Files;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.schedule.biz.MyJob;
import com.kayak.schedule.dao.FileInfoDao;
import com.kayak.schedule.enums.NoticeEnum;
import com.kayak.schedule.enums.NoticeSonEnum;
import com.kayak.schedule.model.NoticeModel;
import com.kayak.schedule.utils.ZipUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author song
 *
 */
@Slf4j
@Component
@RefreshScope
public class TransferToLC extends MyJob {
	@Autowired
	private FileInfoDao fileInfoDao;

	@Value("${lcd.file.baseDir}")
	private String baseDir;

	@Value("${lcd.file.zipPath}")
	private String zipPath;

	@Value("${lcd.file.limitSize}")
	private String limitSize;

	static Map<String, String> serialMap = new HashMap<String, String>();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		List<NoticeModel> fileS = new ArrayList<NoticeModel>();
		try {
			if (checkIp()) {
				super.execute(context);
				// 获取 理财登 “发布队列”中公告
				List<SqlRow> notices = fileInfoDao.queryLCDNotices();
				if(CollectionUtil.isEmpty(notices))
					return;
				
				for (SqlRow notice : notices) {
					SqlRow noticeVersion = fileInfoDao.queryNoticeMaxV(notice);
					if (noticeVersion == null)
						return;
					NoticeModel noticeModel = new NoticeModel();
					noticeModel = BeanUtil.mapToBean(noticeVersion, NoticeModel.class, true);
					if(new File(noticeModel.getFilePath()).exists()) {
						fileS.add(noticeModel);
					}

				}
				//处理公告文件，并压缩
				handleNotice(fileS) ;
				
				DaoUtil.doTrans(() -> {
					// 更新 理财登文件推送状态为 ---> 发布推送中
					for(NoticeModel notice :fileS) {
						fileInfoDao.update("update idb_disclosure_notice set lcd_status = '2' where id = $S{t8DisclosureNoticeId}",
								notice);
					}

				});
				

			}
		} catch (Exception e) {
			log.error("同步到理财登，同文件异常【{}】", e.toString());
			e.printStackTrace();
		}
	}

	//获取文件流水号
	public static String getSerialNo() {
		String key = DateUtil.getNowDate();
		String valStr = serialMap.get(key);
		int num = 1;
		if (!Strings.isEmpty(valStr)) {
			num = Integer.parseInt(valStr);
			num++;
		}
		serialMap.put(key, num + "");
		return StringUtils.leftPad(num + "", 3, "0");

	}

	//处理公告文件，并压缩
	public void handleNotice(List<NoticeModel> notices) throws Exception {
		File baseDirF = new File(baseDir);
		int firstDirNum = 1;
		//baseDir目录下的第一个文件夹
		File fileDir = new File(baseDir + File.separator + firstDirNum);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		for (NoticeModel notice : notices) {
			// 判断当前文件夹文件+当前文件是否 大于限制长度
			if (checkSize(fileDir, notice.getFilePath())) {
				copyFile( notice , fileDir);

			} else {
				//创建下一个文件夹
				firstDirNum++;
				fileDir = new File(baseDir + File.separator + firstDirNum);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				copyFile(notice , fileDir);

			}

		}
		
		// 压缩文件
		String [] dirNames  = baseDirF.list();
		for(String dir : dirNames) {
			String zipFilePath = zipPath + File.separator + "Z70014" + "-" + DateUtil.getNowDate() + "-1-"
					+ getSerialNo() + ".zip";

			ZipUtil.zipCompress(zipFilePath, new File(baseDir+ File.separator+dir));
			
			File okFile = new File (zipFilePath.concat(".ok"));
			if(!okFile.exists())
				okFile.createNewFile();
		}
		FileUtil.deleteContents(baseDirF);
		

	}
	
	/**
	 * 将信披公告复制到指定文件夹
	 * @param notice 公告信息
	 * @param fileDir  目标文件夹
	 * @throws IOException
	 */
	public void copyFile(NoticeModel notice ,File fileDir) throws IOException {
		String filePath = notice.getFilePath();
		String disclosureType = notice.getDisclosureType();
		String disclosureSonType = notice.getDisclosureSonType();
		String registCode = notice.getRegistCode();
		if ("5".equals(disclosureType)) {
			disclosureType = NoticeEnum.getNoticeType(disclosureType).getDesc() + "-"
					+ NoticeSonEnum.getNoticeType(disclosureSonType).getDesc();
		} else {
			disclosureType = NoticeEnum.getNoticeType(disclosureType).getDesc();
		}

		File originFile = new File(filePath);
		String fileName = originFile.getName();
		fileName = registCode + "-" + disclosureType + "-" + fileName;
		File targetFile = new File(fileDir + File.separator + fileName);
		Files.copy(new File(filePath), targetFile);
	}

	/**
	 * 判断（当前文件夹+当前文件）是否 大于限制长度
	 * @param dir 前文件夹
	 * @param filePath 需要移入的文件全路径
	 * @return
	 */
	public boolean checkSize(File dir, String filePath) {
		long limit = Long.parseLong(limitSize);
		long dirSize = 0;
		if (dir.list().length == 0)
			return true;
		for (File file : dir.listFiles()) {
			dirSize += file.length();
		}
		File currentFile = new File(filePath);
		if ((dirSize + currentFile.length()) > limit)
			return false;
		return true;

	}


}
