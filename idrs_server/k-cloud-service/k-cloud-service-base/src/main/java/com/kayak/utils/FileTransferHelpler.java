package com.kayak.utils;

import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.factorys.FileTransferFactory;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.springframework.stereotype.Component;

@Component
public class FileTransferHelpler {

    FileTransferConfig fileTransferConfig =  ApplicationContextHolder.getBean(FileTransferConfig.class);

    public  FileTransfer getTransfer() throws Exception{
        return  FileTransferFactory.createFileTransfer(fileTransferConfig);
    }
}

