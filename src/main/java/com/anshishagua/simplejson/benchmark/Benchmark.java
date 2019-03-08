package com.anshishagua.simplejson.benchmark;

import com.anshishagua.simplejson.JSON;
import com.anshishagua.simplejson.types.JSONArray;
import com.anshishagua.simplejson.types.JSONNull;
import com.anshishagua.simplejson.types.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {
    public final int RUNS = 100;
    public final boolean HUMAN_OUTPUT = true;
    public int BATCH = 1000;

    private JSONObject createSmallObject(){
        JSONObject obj=new JSONObject();
        obj.put("key1","value1");
        obj.put("key2",(int)(Math.random()*1000000));
        obj.put("key3",Math.random());
        obj.put("key4",false);
        obj.put("key5", JSONNull.NULL);
        return obj;
    }

    private JSONObject createMediumObject(int i){
        JSONObject outer=new JSONObject();
        outer.put("id","deadbeef-dead-beef-dead-beef00000001");
        outer.put("type","MediumObject");
        JSONArray a = new JSONArray();
        a.put(createSmallObject());
        a.put(createSmallObject());
        outer.put("data",a);
        outer.put("serial",i);
        outer.put("created",new java.util.Date().toString());
        return outer;
    }

    private JSONObject createLargeObject(){
        JSONObject obj = new JSONObject();
        JSONObject device=new JSONObject();
        device.put("MMMInfo","iPhone7,2");
        device.put("DeviceType","ios");
        device.put("DeviceOSVersion","9.3.2");
        device.put("DeviceId","deadbeef-dead-beef-dead-beef00000001");
        device.put("BinaryVersion","9.99.99");
        obj.put("Device",device);
        obj.put("Created",System.currentTimeMillis());
        JSONObject session=new JSONObject();
        session.put("EventId",(int)(Math.random()*1000));
        session.put("SessionId","deadbeef-dead-beef-dead-beef00000002");
        obj.put("Session",session);
        JSONObject context=new JSONObject();
        context.put("Identifier","statusUpdate");
        JSONObject metadata=new JSONObject();
        metadata.put("ActivityId",(int)(Math.random()*10000000));
        context.put("MetaData",metadata);
        context.put("MetricVersion",99);
        obj.put("Context",context);
        JSONObject user=new JSONObject();
        user.put("GlobalUserId","deadbeef-dead-beef-dead-beef00000003");
        obj.put("User",user);
        JSONObject application=new JSONObject();
        application.put("ApplicationId","deadbeef-dead-beef-dead-beef00000004");
        application.put("BundleId","deadbeef-dead-beef-dead-beef00000005");
        obj.put("Application",application);
        obj.put("SchemaVersion",1);
        return obj;
    }

    public void runTest(String name,Runnable test){
        try{
            // Run warmup
            test.run();
            Thread.sleep(100);
            for(int i=0;i<20;i++){
                test.run();
            }
            Thread.sleep(100);
            System.gc();

            long[] times=new long[RUNS];
            long min=-1;
            long max=0;

            for(int i=0;i<RUNS;i++){
                long pre=System.nanoTime();
                test.run();
                long post=System.nanoTime();
                times[i]=post-pre;
                System.gc();
                Thread.sleep(100);
            }

            if(HUMAN_OUTPUT){
                long total=0;
                for(int i=0;i<RUNS;i++){
                    total+=times[i];
                    if(min==-1){
                        min=times[i];
                    }else{
                        if(times[i]<min){
                            min=times[i];
                        }
                    }
                    if(times[i]>max){
                        max=times[i];
                    }
                }
                java.util.Arrays.sort(times);
                System.out.println(" + "+name+" min:"+(min/1000000.0)+" max:"+(max/1000000.0)+" avg:"+(((total/(float)RUNS))/1000000.0)+" med:"+(times[times.length/2]/1000000.0));
            }else{
                StringBuilder buf=new StringBuilder();
                buf.append(name);
                for(long l:times){
                    buf.append(", ");
                    buf.append(l);
                }
                System.out.println(buf.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void testRawParsing(final String data) throws Exception{
        runTest("Simplejson",new Runnable(){
            public void run(){
                JSONArray jsonArray = JSON.parseArray(data);
            }
        });
    }

    public void testSmallObjectAccess(final String data) throws Exception{
        List<String> key1=new ArrayList<>(BATCH);
        List<Integer> key2=new ArrayList<Integer>(BATCH);
        List<Double> key3=new ArrayList<Double>(BATCH);
        List<Boolean> key4=new ArrayList<Boolean>(BATCH);

        runTest("Simplejson", new Runnable(){
            public void run(){
                key1.clear();key2.clear();key3.clear();key4.clear();
                JSONArray arr= JSON.parseArray(data);

                for(int i=0;i<arr.length();i++){
                    JSONObject obj= (JSONObject) arr.get(i);
                    key1.add(obj.getString("key1"));
                    key2.add(obj.getInt("key2"));
                    key3.add(obj.getDouble("key3"));
                    key4.add(obj.getBoolean("key4"));
                }
            }
        });
    }

    public void testSmallObjectParsing(final String data) throws Exception{
        runTest("Simplejson",new Runnable(){
            public void run(){
                SmallObject[] arr=JSON.parse(data,SmallObject[].class);
            }
        });
    }

    public void testSmallObjectSplit(final String data) throws Exception{
        final List<String> result=new ArrayList<String>(BATCH);
        runTest("GSON class based",new Runnable(){
            public void run(){
                result.clear();
                SmallObject[] arr=JSON.parse(data,SmallObject[].class);
                for(SmallObject obj:arr){
                    result.add(JSON.toJSONString(obj));
                }
            }
        });
    }

    public void testMediumObjectParsing(final String data) throws Exception{
        runTest("Simplejson",new Runnable(){
            public void run(){
                MediumObject[] arr=JSON.parse(data,MediumObject[].class);
            }
        });
    }

    public void testMediumObjectSplit(final String data) throws Exception{
        final List<String> result=new ArrayList<String>(BATCH);
        runTest("GSON class based",new Runnable(){
            public void run(){
                result.clear();
                MediumObject[] arr=JSON.parse(data,MediumObject[].class);
                for(MediumObject obj:arr){
                    result.add(JSON.toJSONString(obj));
                }
            }
        });
    }

    public void testLargeObjectParsing(final String data) throws Exception{
        runTest("GSON class based",new Runnable(){
            public void run(){
                LargeObject[] arr=JSON.parse(data,LargeObject[].class);
            }
        });
    }

    public void testLargeObjectSplit(final String data) throws Exception{
        final List<String> result=new ArrayList<String>(BATCH);
        runTest("Simplejson",new Runnable(){
            public void run(){
                result.clear();
                LargeObject[] arr=JSON.parse(data,LargeObject[].class);
                for(LargeObject obj:arr){
                    result.add(JSON.toJSONString(obj));
                }
            }
        });
    }

    public void testSplit(final String data) throws Exception{
        final List<String> result=new ArrayList<String>(BATCH);
        runTest("Simplesjon",new Runnable(){
            public void run(){
                result.clear();
                JSONArray arr=JSON.parseArray(data);
                for(int i=0;i<arr.length();i++){
                    result.add(arr.get(i).toString());
                }
            }
        });
    }


    public Benchmark(){
        try{
            System.out.println(createLargeObject().toString());
            System.out.println("Generating SmallObject batch data");
            JSONArray array=new JSONArray();
            for(int i=0;i<BATCH;i++){
                array.put(createSmallObject());
            }
            final String raw_batch_1=JSON.toJSONString(array);
            System.out.println(raw_batch_1);
            array=null;
            System.out.println(" + Data size "+raw_batch_1.length());
            System.out.println("Running SmallObject parse test");
            testRawParsing(raw_batch_1);

            testSmallObjectParsing(raw_batch_1);

            System.out.println("Running SmallObject split and serialize test");
            testSplit(raw_batch_1);
            testSmallObjectSplit(raw_batch_1);

            System.out.println("Running SmallObject parse and access test");
            testSmallObjectAccess(raw_batch_1);

            System.out.println("Generating MediumObject batch data");
            array=new JSONArray();
            for(int i=0;i<BATCH;i++){
                array.put(createMediumObject(i));
            }
            final String raw_batch_2=JSON.toJSONString(array);
            array=null;
            System.out.println(" + Data size "+raw_batch_2.length());
            System.out.println("Running MediumObject parse test");
            testRawParsing(raw_batch_2);
            testMediumObjectParsing(raw_batch_2);

            System.out.println("Running MediumObject split and serialize test");
            testSplit(raw_batch_2);
            testMediumObjectSplit(raw_batch_2);

            System.out.println("Generating LargeObject batch data");
            array=new JSONArray();
            for(int i=0;i<BATCH;i++){
                array.put(createLargeObject());
            }
            final String raw_batch_3=JSON.toJSONString(array);
            array=null;
            System.out.println(" + Data size "+raw_batch_3.length());
            System.out.println("Running LargeObject parse test");
            testRawParsing(raw_batch_3);
            testLargeObjectParsing(raw_batch_3);

            System.out.println("Running LargeObject split and serialize test");
            testSplit(raw_batch_3);
            testLargeObjectSplit(raw_batch_3);
        }catch(Throwable t){
            t.printStackTrace();
        }

    }

    public static void main(String [] args) {
        new Benchmark();
    }
}
