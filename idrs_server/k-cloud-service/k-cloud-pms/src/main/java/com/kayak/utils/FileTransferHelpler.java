package com.kayak.utils;

import com.kayak.pms.opFlow.engine.context.ApplicationContextHolder;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.factorys.FileTransferFactory;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;

public class FileTransferHelpler {

    public FileTransfer getTransfer() throws Exception {
        FileTransferConfig fileTransferConfig = ApplicationContextHolder.getBean(FileTransferConfig.class);
        return  FileTransferFactory.createFileTransfer(fileTransferConfig);
    }
}

