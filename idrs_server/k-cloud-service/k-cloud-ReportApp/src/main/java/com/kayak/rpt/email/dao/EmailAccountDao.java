package com.kayak.rpt.email.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.email.model.EmailAccount;
import org.springframework.stereotype.Repository;

@Repository
public class EmailAccountDao extends ComnDao {
    public EmailAccount selectVoById(String userId){
       //TODO 待补全
       EmailAccount emailAccount = new EmailAccount();
       return  emailAccount;
    };

     public int insertOrUpdate(EmailAccount emailAccount){
        //TODO 待补全
        int count = 1;
        return  count;
    };
}
