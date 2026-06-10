package com.kayak.pms.email.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.email.model.M8DisclosureManual;
import org.springframework.stereotype.Repository;

@Repository
public class M8DisclosureManualDao extends ComnDao {

    public int insertManualInfo(M8DisclosureManual m8DisclosureManual) throws Exception {
        return super.update("insert into idb_disclosure_notice_config(`ID`,`CRT_DATE`,`CRT_USER`,`TITLE`,`FILE_NAME`,`FILE_PATH`,`TYPE`,`SON_TYPE`,`PROD_CODE`,`START_ESTABLISHDATE`,`SENDEMAIL`,`CHANNEL`,`NOTE`,`CURRENT`,`APP_STATUS`,`POST_STATUS`) values ($AUTOIDS{id}, $S{crtDate}, $S{crtUser}, $S{title}, $S{fileName}, $S{filePath}, $S{type},$S{sonType},$S{prodCode}, $S{startDstablishdate}, $S{sendmail}, $S{channel}, $S{note},'04','02','00')".toString(),m8DisclosureManual).getEffect();
    }
}
