package com.kayak.pms.opFlow.engine.entity;

import com.kayak.pms.opFlow.engine.constant.ActorTypeConstant;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 12/04/2017.
 */
@Data
@Alias("taskActor")
public class TaskActor {
    private String id;
    private String actorId;
    private String taskId;
    //角色id-1,  指定具体人id-2，指定人的处理器id-3, 指定角色处理器-4
    private String actorType;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("任务id:").append(taskId);
        if(ActorTypeConstant.ROLE.equals(actorType)){
            sb.append(",参与者角色ID:").append(actorId);
        }
        if(ActorTypeConstant.ORDINARY.equals(actorType)){
            sb.append(",指定参与者ID:").append(actorId);
        }
        if(ActorTypeConstant.USER_IDS_BY_ASSIGNMENT_HANDLER.equals(actorType)){
            sb.append(",类处理器指定的人:").append(actorId);
        }
        if(ActorTypeConstant.ROLE_IDS_BY_ASSIGNMENT_HANDLER.equals(actorType)){
            sb.append(",处理器指定角色:").append(actorId);
        }
        return sb.toString();
    }
}
