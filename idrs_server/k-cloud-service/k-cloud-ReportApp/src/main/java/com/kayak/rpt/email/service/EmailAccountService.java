package com.kayak.rpt.email.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.rpt.email.dao.EmailAccountDao;
import com.kayak.rpt.email.model.EmailAccount;
import com.kayak.rpt.rhzj.model.ReportPCD;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
@APIDefine(desc = "邮件账户服务", model = EmailAccount.class)
public class EmailAccountService {
    @Resource
    private EmailAccountDao emailAccountDao;

    /**
     * 根据条件查询 当前发邮件账户   TODO 条件待定
     * @param userId
     * @return
     */
    public EmailAccount getCurrentEmailAccount(String userId){
        Object obj = emailAccountDao.selectVoById( userId);
        EmailAccount emailAccount= new EmailAccount();
        if(obj==null){
            return emailAccount;
        }
        emailAccount = (EmailAccount)obj;
        return emailAccount;
    }


    /**
     * 根据条件查询 当前发邮件账户
     * @param emailAccount
     * @return
     */
    public int insertOrUpdateEmailAccount(EmailAccount emailAccount){
        return emailAccountDao.insertOrUpdate(emailAccount);
    }
}
