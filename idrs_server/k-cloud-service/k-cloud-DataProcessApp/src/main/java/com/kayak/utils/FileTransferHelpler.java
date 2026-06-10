package com.kayak.utils;

import com.kayak.context.ApplicationContextHolder;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.factorys.FileTransferFactory;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.springframework.stereotype.Component;

public class FileTransferHelpler {

    static FileTransferConfig fileTransferConfig;

    public static FileTransfer getTransfer() throws Exception {
        fileTransferConfig =  ApplicationContextHolder.getBean(FileTransferConfig.class);
        return  FileTransferFactory.createFileTransfer(fileTransferConfig);
    }

    public static FileTransfer getTransfer(FileTransferConfig fileTransferConfig) throws Exception {
        return  FileTransferFactory.createFileTransfer(fileTransferConfig);
    }

}

