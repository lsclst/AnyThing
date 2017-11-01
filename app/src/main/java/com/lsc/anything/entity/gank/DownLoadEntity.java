package com.lsc.anything.entity.gank;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lsc.anything.utils.DownLoadUtil;

/**
 * Created by lsc on 2017/10/10 0010.
 *
 * @author lsc
 */
@DatabaseTable(tableName = "download-detail")
public class DownLoadEntity {

    public static final String COL_N_ID = "entityId";
    public static final String COL_N_TYPE = "type";
    public static final String COL_N_URI = "uri";
    public static final String COL_N_PATH = "localPath";

    @DatabaseField(columnName = COL_N_ID, id = true)
    private long entityId;
    @DatabaseField(columnName = COL_N_TYPE)
    private int Op_type;
    @DatabaseField(columnName = COL_N_URI)
    private String uri;
    @DatabaseField(columnName = COL_N_PATH)
    private String localPath;

    public DownLoadEntity() {
    }

    public DownLoadEntity(long entityId, int op_type) {
        this.entityId = entityId;
        Op_type = op_type;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public
    @DownLoadUtil.PIC_OP_TYPE
    int getOp_type() {
        return Op_type;
    }

    public void setOp_type(int op_type) {
        Op_type = op_type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public String toString() {
        return "DownLoadEntity{" +
                "entityId=" + entityId +
                ", Op_type=" + Op_type +
                ", uri='" + uri + '\'' +
                ", localPath='" + localPath + '\'' +
                '}';
    }
}
