package com.kayak.core.desensitized;

import com.kayak.core.util.Tools;

/**
 * 描述:  证件,电话脱敏
 * 创建人: axin
 * 创建时间:2021年3月13日上午11:47:04
 */
public class IdCodeDesensitized implements Desensitized {

    @Override
    public String desensitized(Object value) {
        String _vaule = Tools.obj2Str(value);
        if (Tools.strIsEmpty(_vaule) || _vaule.length() < 6) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        int z , j ;
        if (_vaule.length() < 8 && _vaule.length() >= 6) {
            j = 2 ; z = 1 ;
        }else if (_vaule.length() < 12 && _vaule.length() >= 8) {
            j = 2 ; z = 2 ;
        }else if (_vaule.length() < 16 && _vaule.length() >= 12) {
            j = 3 ; z = 3 ;
        }else {
            j = 4 ; z = 4 ;
        }
        for (int i = 0 ; i < _vaule.length() - j - z ; i ++) {
            sb.append("*");
        }
        return _vaule.substring(0, j) + sb + _vaule.substring(_vaule.length() - z);
    }

}
