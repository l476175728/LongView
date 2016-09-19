package com.example.liuhailong.longviewsample.bean;

import java.util.List;

/**
 * Created by liuhailong on 16/6/29.
 */
public class ResultBean {


    private String message;
    private String success;
    private List<Photo> data;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setData(List<Photo> data) {
        this.data = data;
    }

    public String getMessage() {

        return message;
    }

    public String getSuccess() {
        return success;
    }

    public List<Photo> getData() {
        return data;
    }

    public class Photo{

        private String createId;
        private String createTime;
        private String emeId;
        private String fileId;
        private String fileName;
        private String filePath;
        private String fileType;



        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setEmeId(String emeId) {
            this.emeId = emeId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getCreateId() {

            return createId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getEmeId() {
            return emeId;
        }

        public String getFileId() {
            return fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getFileType() {
            return fileType;
        }

//        @Override
//        public int compare(Photo photo1, Photo photo2) {
//
//            long time1=getSureTime(photo1.getCreateTime());
//            long time2=getSureTime(photo2.getCreateTime());
//            if(time1>=time2){
//                return 0;
//            }else{
//                return 1;
//            }
//
//        }
        @Override
        public String toString() {
            return "Photo{" +
                    "createTime='" + createTime + '\'' +
                    '}';
        }
    }


}
