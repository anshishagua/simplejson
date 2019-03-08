package com.anshishagua.simplejson.benchmark;

public class LargeObject{
    public long Created;
    public String MetricType;
    public int SchemaVersion;
    public DeviceObject Device;
    public SessionObject Session;
    public ContextObject Context;
    public UserObject User;
    public ApplicationObject Application;

    public class DeviceObject{
        public String MMMInfo;
        public String DeviceType;
        public String DeviceOSVersion;
        public String DeviceId;
        public String BinaryVersion;
    }

    public class SessionObject{
        public int EventId;
        public String SessionId;
    }

    public class ContextObject{
        public String Identifier;
        public MetaDataObject MetaData;
        public int MetricVersion;
    }

    public class MetaDataObject{
        public int ActivityId;
    }

    public class UserObject{
        public String GlobalUserId;
    }

    public class ApplicationObject{
        public String ApplicationId;
        public String BundleId;
    }
}
