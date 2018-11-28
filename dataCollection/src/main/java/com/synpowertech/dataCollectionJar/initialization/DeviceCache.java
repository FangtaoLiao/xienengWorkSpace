package com.synpowertech.dataCollectionJar.initialization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.synpowertech.dataCollectionJar.dao.CollJsonComSetMapper;
import com.synpowertech.dataCollectionJar.dao.CollJsonDatacollSetMapper;
import com.synpowertech.dataCollectionJar.dao.CollJsonDevSetMapper;
import com.synpowertech.dataCollectionJar.dao.CollJsonModelDetailExtendMapper;
import com.synpowertech.dataCollectionJar.dao.CollJsonTypeMapper;
import com.synpowertech.dataCollectionJar.dao.CollModelDetailMqttMapper;
import com.synpowertech.dataCollectionJar.dao.CollModelMapper;
import com.synpowertech.dataCollectionJar.dao.CollSignalLabelMapper;
import com.synpowertech.dataCollectionJar.dao.CollYxExpandMapper;
import com.synpowertech.dataCollectionJar.dao.CollectorMessageStructureMapper;
import com.synpowertech.dataCollectionJar.dao.DeviceMapper;
import com.synpowertech.dataCollectionJar.dao.FieldSignalGuidMappingMapper;
import com.synpowertech.dataCollectionJar.domain.AddressIdGuid;
import com.synpowertech.dataCollectionJar.domain.CollDeviceLocation;
import com.synpowertech.dataCollectionJar.domain.CollJsonDevSet;
import com.synpowertech.dataCollectionJar.domain.CollJsonModelDetailExtend;
import com.synpowertech.dataCollectionJar.domain.CollModelDetailMqtt;
import com.synpowertech.dataCollectionJar.domain.CollSignalLabel;
import com.synpowertech.dataCollectionJar.domain.CollYxExpand;
import com.synpowertech.dataCollectionJar.domain.CollectorMessageStructure;
import com.synpowertech.dataCollectionJar.domain.Device;
import com.synpowertech.dataCollectionJar.domain.FieldSignalGuidMapping;
import com.synpowertech.dataCollectionJar.mqttJsonClient.MqttActiveConnectClient;
import com.synpowertech.dataCollectionJar.utils.AddressComparator;
import com.synpowertech.dataCollectionJar.utils.CacheMappingUtil;
import com.synpowertech.dataCollectionJar.utils.SunRiseSet;
import com.synpowertech.dataCollectionJar.utils.Table2modelUtil;

public class DeviceCache implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DeviceCache.class);

    private static DeviceCache deviceCache;

    //注：加入储能设备后，采集器下挂设备就不止一层了，这样需要透传，对于解析来说，最底层设备的上级仍然是数采

    /**
     * 采集节点id(String)和下级设备id列表的缓存,和设备是否启用状态无关，对时在用,不用
     */
//	public static Map<String, Set<Integer>> collector2devSet = null;

    /**
     * 设备id和上级采集节点id(String)的对应关系,封装下发指令的第一个缓存(光伏端不传collectorId时使用),目前没用
     */
//	public static Map<Integer, String> dev2collector = null;

    /**
     * 封装下发指令需要的的第二个缓存Map<signal_guid,Map<pid,控制类型>>
     */
//	public static Map<String, Map<Integer, String>> signalGuid2pId = null;

    /**
     * 数据采集节点id(String)集合，数采关联关系生成解析点在使用
     */
//	public static Set<String> collectorIdSet = null;

    /**
     * TODO 数据采集节点id(String)集合，对时使用，无关状态，还有新数采动态订阅
     */
    public static Set<String> oldCollectorIdSet = null;
    public static List<String> newCollectorIdList = null;

    /**
     * TODO 采集节点id和数据采集器数据库id对应关系,Map<dataColl_id,deviceId>,解析时使用;
     */
    public static Map<String, Integer> collectorId2devId = null;

    /**
     * TODO 旧的畅洋数采sn和id对应Map，解析旧数采
     */
    public static Map<String, Integer> oldCollSnIdMap = null;

    /**
     * TODO 新数采sn和id对应Map，解析新数采，
     */
    public static Map<String, Integer> newCollSnIdMap = null;

    /***************************************************************************/

    /**
     * 为解析guid的四个缓存准备,Map<dataModel, Map<addressId, List<guid>>>
     */
    public static Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYc = null;
    public static Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYx = null;
    public static Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYk = null;
    public static Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYt = null;

    /**
     * 旧数采解析缓存
     * 可算出临界的pid(crisis pid),自己填充
     * 解析所需的四个映射缓存 Map<collector_sn + type，<pid,List<signalGuid>>四种，遥测和遥信，遥控遥调 ，collector_sn加类型便于自己分辨
     * 解析获取设备id所需的四个映射缓存 Map<collector_sn + type，<pid,devId>>四种，遥测和遥信，遥控遥调，collector_sn加类型便于自己分辨
     */
    public static Map<String, Map<Integer, List<String>>> collector2pidGuidYc = null;
    public static Map<String, Map<Integer, List<String>>> collector2pidGuidYx = null;
    public static Map<String, Map<Integer, List<String>>> collector2pidGuidYk = null;
    public static Map<String, Map<Integer, List<String>>> collector2pidGuidYt = null;

    public static Map<String, Map<Integer, Integer>> collector2pidDevIdYc = null;
    public static Map<String, Map<Integer, Integer>> collector2pidDevIdYx = null;
    public static Map<String, Map<Integer, Integer>> collector2pidDevIdYk = null;
    public static Map<String, Map<Integer, Integer>> collector2pidDevIdYt = null;

    //旧数采封装下发所需
    /**
     * 设备id,guid找到封装下发的pid,Map<devId,<guid,pid>>
     * 设备id,guid找到遥调遥控中封装下发的类型,Map<devId,<guid,ctrlType>>
     */
    public static Map<Integer, Map<String, Integer>> devId2guidPid = null;
    public static Map<Integer, Map<String, String>> devId2guidCtrlType = null;

    /***************************************************************************/


    /**
     * Map<signalNameId,Map<实体名，实体属性名>>,通用
     */
    public static Map<String, Map<String, String>> signalGuid2table = null;


    /**
     * 变位遥信存储对应关系Map<signal_guid,Map<value,yxId>>,通用
     */
    public static Map<String, Map<Integer, Integer>> signalGuid2yxId = null;

    /**
     * TODO 增加遥信id的列表，便于实现电站严重告警状态
     */
    public static List<Integer> yxIdAlarmList = null;

    /**
     * 设备id和其归属电站Id,Map<deviceId,plantId>,通用
     */
    public static Map<Integer, Integer> devId2plantId = null;

    /**
     * TODO signalGuid中有符号数缓存,用在新数采解析上,通用
     */
    public static List<String> signalGuid2signedNum = null;

    /**
     * signalGuid与增益，修正系数，最小值，最大值得映射,通用
     */
    public static Map<String, Float> signalGuid2gain = null;
    public static Map<String, Float> signalGuid2factor = null;
    public static Map<String, Float> signalGuid2minVal = null;
    public static Map<String, Float> signalGuid2maxVal = null;

    /**
     * 遥测特殊处理方法，遥测表遥信特殊处理bit起始位，subString读取终止位(读取位后一位),通用
     */
    public static Map<String, String> signalGuid2processMethod = null;
    public static Map<String, Map<Integer, Integer>> signalGuid2processDetail = null;

    /**
     * 单寄存器信号缓存列表，目的是规避数据采集器在设备掉线时仍然转发计算式数据
     */
    public static List<String> signalGuid2singleRegister = null;

    /**
     * 遥测表遥信缓存列表，目的是解析时转化成整型存入，List中存guid,通用
     */
    public static List<String> signalGuid4yc2yx = null;

    /**
     * 设备id与其当前日出日落时间对应关系,前后各加一个小时作为误差，日出在前，日落在后,只查询逆变器,通用
     */
    public static Map<Integer, Long[]> devId2SunRiseSet = null;

    /**
     * 示例电站数采id列表
     */
    public static List<Integer> exampleCollList = null;

    /**
     * 示例电站逆变器设备id
     */
    public static List<Integer> exampleInverterList = null;

    /**********************************************************/
    //23点天黑，记录使用60KTL的设备的今日总发电量数据,数据实现在对时任务一起，通用
    public static HashMap<String, String> dailyEnergyMap = new HashMap<String, String>(5);

    /**
     * TODO 新数采动态订阅相关缓存，分组划分，Qos和订阅主题划分
     */
    public static List<MqttActiveConnectClient> clientList = new ArrayList<MqttActiveConnectClient>(5);
    public static Stack<MqttActiveConnectClient> clientStack = new Stack<MqttActiveConnectClient>();
    public static List<String[]> subTopicList = null;
    public static List<int[]> subQosList = null;
    //新数采订阅所需要的连接数
    public static int newClientCount = 0;
    //新数采控制下发所需，collsn和订阅发布客户端下标关系
    public static Map<String, Integer> collSn2ClientIdx = null;


    // TODO 新数采解析缓存
    /**
     * TODO 数采id,comId，salveId找到设备id ，Map<coll_id,Map<comId_slaveId, devId>>
     */
    public static Map<Long, Map<String, Long>> slaveId2devIdMap = null;

    /**
     * TODO 新数采封装下发所需缓存Map<devId,coll_id_comId_slaveId>
     */
    public static Map<Long, String> devId2slaveIdMap = null;
    /**
     * TODO 新数采封装下发所需缓存,整理数据用Map<devId,coll_id_comId>
     */
//	public static Map<Long,String> devId2comIdMap = null;

    /**
     * TODO  新旧数采，设备和点表对应关系Map<deviceId,dataMode>,通用
     */
    public static Map<Integer, Integer> devId2dataModeMap = null;

    /**
     * TODO  新数采解析缓存Map<dataModel, Map<addressId, List<guid>>
     */
    public static Map<Integer, Map<String, List<String>>> dataModel2addrGuid = null;

    /**
     * TODO 新数采合成点高位地址Map<dataModel,List<addrH>>
     */
    public static Map<Integer, List<String>> addrHighListMap = null;

    /**
     * TODO 新数采下发点表缓存:Map<dataModel,List<addr + 03/06.. + 1/0>>(只包含遥测遥信),不重复，改用Set;
     */
//	public static Map<Integer,List<String>> addrListMap = null;
    public static Map<Integer, Set<String>> addrSetMap = null;

    /**
     * TODO 新数采下发控制指令缓存:Map<guid,functionCode>(只包含遥控遥调);
     */
    public static Map<String, String> guidCodeMap = null;

    /**
     * TODO 新数采控制缓存:通过collId和deviceId可反向查找comid和slaveId;guid和高位地址缓存Map<signalGuid,addrHigh>,只包含遥控遥调
     */
    public static Map<String, String> guidAddrHighMap = null;

    /**
     * 所有设备id用于定时判断redis中设备通讯状态缓存,List<devId>
     */
    public static List<Integer> devIdList = new ArrayList<Integer>();

    /**
     * @Author lz
     * @Description: 用于特殊判断的设备, 当设备在此时, 无需判断状态, 直接设置状态为运行
     * @Date: 2018/10/25 19:54
     **/
    public static List<Integer> devJoinIdList = new ArrayList<>();

    /**********************************************************/
    //TODO 畅洋数采手动合成点所需缓存,一般只在遥测出现
    //TODO 畅洋数采合成点信号高位Map<dataModel,signalGuidH:signalGuidL>
    public static Map<Integer, Map<String, String>> signalGuidHMap = null;
    //TODO 畅洋数采合成点信号低位Map<dataModel,signalGuidL:signalGuidH>
    public static Map<Integer, Map<String, String>> signalGuidLMap = null;

    /**********************************************************/


    // 手动注入，设为类变量
    @Autowired
    private CollModelDetailMqttMapper collModelDetailMqttMapperTemp;
    @Autowired
    private CollYxExpandMapper collYxExpandMapperTemp;
    @Autowired
    private FieldSignalGuidMappingMapper fieldSignalGuidMappingMapperTemp;
    @Autowired
    private CollSignalLabelMapper collSignalLabelMapperTemp;
    @Autowired
    private DeviceMapper deviceMapperTemp;
    @Autowired
    private CollectorMessageStructureMapper collectorMessageStructureMapperTemp;
    @Autowired
    private CollModelMapper collModelMapperTemp;

    @Autowired
    private CollJsonComSetMapper collJsonComSetMapperTemp;
    @Autowired
    private CollJsonDatacollSetMapper collJsonDatacollSetMapperTemp;
    @Autowired
    private CollJsonDevSetMapper collJsonDevSetMapperTemp;
    @Autowired
    private CollJsonModelDetailExtendMapper collJsonMDExtMapperTemp;
    @Autowired
    private CollJsonTypeMapper collJsonTypeMapperTemp;

    static CollModelDetailMqttMapper collModelDetailMqttMapper;
    static CollYxExpandMapper collYxExpandMapper;
    static FieldSignalGuidMappingMapper fieldSignalGuidMappingMapper;
    static CollSignalLabelMapper collSignalLabelMapper;
    static DeviceMapper deviceMapper;
    static CollectorMessageStructureMapper collectorMessageStructureMapper;
    static CollModelMapper collModelMapper;

    static CollJsonComSetMapper collJsonComSetMapper;
    static CollJsonDatacollSetMapper collJsonDatacollSetMapper;
    static CollJsonDevSetMapper collJsonDevSetMapper;
    static CollJsonModelDetailExtendMapper collJsonMDExtMapper;
    static CollJsonTypeMapper collJsonTypeMapper;

    /**
     * @Title: refreshDeviceCache
     * @Description: 刷新设备列表和缓存
     * @return: boolean
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月16日下午3:42:27
     */
    public static boolean refreshDeviceCache() {

        try {
            deviceCache.deviceCacheInitial();
            deviceCache.persistenceCacheInitial();
            deviceCache.dataProcessingCache();
            deviceCache.specialProcessCache();
            deviceCache.sunRiseSetCache();
            //示例电站
            deviceCache.exampleDeviceCache();

            //新数采解析缓存，最后执行，依赖其他缓存
            deviceCache.newCollParseCache();

            logger.debug("Refresh the deviceCache  successfully!");
            return true;
        } catch (Exception e) {
            logger.error("Refresh the deviceCache  unsuccessfully：{}", e);
        }
        return false;
    }

    public void afterPropertiesSet() throws Exception {
        DeviceCache.deviceCache = new DeviceCache();
        logger.info("DeviceCacheInstance is created successfully!");
    }

    public void initMethod() {

        // 注入后赋给静态变量
        collModelDetailMqttMapper = collModelDetailMqttMapperTemp;
        collYxExpandMapper = collYxExpandMapperTemp;
        fieldSignalGuidMappingMapper = fieldSignalGuidMappingMapperTemp;
        collSignalLabelMapper = collSignalLabelMapperTemp;
        deviceMapper = deviceMapperTemp;
        collectorMessageStructureMapper = collectorMessageStructureMapperTemp;
        collModelMapper = collModelMapperTemp;

        collJsonComSetMapper = collJsonComSetMapperTemp;
        collJsonDatacollSetMapper = collJsonDatacollSetMapperTemp;
        collJsonDevSetMapper = collJsonDevSetMapperTemp;
        collJsonMDExtMapper = collJsonMDExtMapperTemp;
        collJsonTypeMapper = collJsonTypeMapperTemp;

        try {
            deviceCacheInitial();
            persistenceCacheInitial();
            dataProcessingCache();
            specialProcessCache();
            sunRiseSetCache();
            //示例电站
            exampleDeviceCache();

            //新数采解析缓存，最后执行，依赖其他缓存
            newCollParseCache();

            logger.info("DeviceCache'Initialization is created successfully!");
        } catch (Exception e) {
            logger.error("DeviceCache'Initialization  gets an exception:{}", e);
        }

    }

    /***************************************************************************************/

    /**
     * @Title: deviceCacheInitial
     * @Description: 封装和解析xml缓存实现
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月3日下午1:47:42
     */
    private void deviceCacheInitial() {

        /*
         * collector2dev.clear(); dev2clooector.clear();
         */

        // 不做清空，刷新时new一个，刷新完毕指向新map

//		Map<String, Set<Integer>> collector2devSetTemp = new ConcurrentHashMap<String, Set<Integer>>();
//		Set<Integer> devSet = new HashSet<Integer>();

//		Map<Integer, String> dev2collectorTemp = new ConcurrentHashMap<Integer, String>();

        //所有数采
//		Set<String> collectorIdSetTemp = new HashSet<String>();
        Set<String> oldCollectorIdSetTemp = new HashSet<String>();
        List<String> newCollectorIdListTemp = new ArrayList<String>();


        // 数据采集器id和采集节点名称对应关系
        Map<String, Integer> collectorId2devIdTemp = new HashMap<String, Integer>();

        //旧畅洋数采sn和id对应Map
        Map<String, Integer> oldCollSnIdMapTemp = new HashMap<String, Integer>(50);

        //新数采sn和id对应Map
        Map<String, Integer> newCollSnIdMapTemp = new HashMap<String, Integer>(50);

        // 封装下发指令的第二个缓存
//		Map<String, Map<Integer, String>> signalGuid2pIdTemp = new HashMap<String, Map<Integer, String>>();

/*		List<Device> resultList = deviceMapper.selectAll();

		if (resultList != null) {
			String collectorIdTemp = "";
			//识别设备归属不同数采，避免多次查询
			Integer supIdTemp = -1;
			for (Device device : resultList) {
				
				Integer supId = device.getSupId();
				//数采和上级设备为空的跳过
				if (supId == null || supId == 0 ) {
					continue;
				}
				
				if ("".equals(collectorIdTemp) || supId != supIdTemp) {
					
					//保存前一个数采的
					if (devSet.size() != 0) {
						collector2devSetTemp.put(collectorIdTemp, devSet);
						devSet = new HashSet<Integer>();
					}
					
					//第一次或归属不同数采 重新赋值
					supIdTemp = supId;
					//处理多层设备,找到最上层数采id
					Device supDevice = deviceMapper.selectByPrimaryKey(supIdTemp);
					Integer supIdTemp1 = supDevice.getSupId();
					while(supIdTemp1 != null && supIdTemp1 != 0 ){
						supIdTemp = supIdTemp1;
						supDevice = deviceMapper.selectByPrimaryKey(supIdTemp);
						supIdTemp1 = supDevice.getSupId();
					}
					String collectorId = supDevice.getDeviceSn();
					collectorIdTemp = collectorId;
					//数采型号id
					Integer collModelId = supDevice.getDeviceModelId();
					
					collectorIdSetTemp.add(collectorIdTemp);
					//新旧数采map，减少数据库查询
					if (collModelId < 3) {
						oldCollSnIdMapTemp.put(collectorIdTemp, supDevice.getId());
					} else {
						newCollSnIdMapTemp.put(collectorIdTemp, supDevice.getId());
					}
					
				}
				Integer devId = device.getId();
//				dev2collectorTemp.put(devId, collectorIdTemp);
				
				devSet.add(devId);
			}
			
			//最后一个数采devSet存入
			if (devSet.size() != 0) {
				collector2devSetTemp.put(collectorIdTemp, devSet);
			}
		}*/
        //获取所有未删除数采
        List<Device> dataCollectors = deviceMapper.selectAllDataColls();

        if (dataCollectors != null) {
            String collSn;
            Integer devId;
            String deviceStatus;

            for (Device dataCollector : dataCollectors) {
                collSn = dataCollector.getDeviceSn();
                devId = dataCollector.getId();
                deviceStatus = dataCollector.getDeviceStatus();
                //区分新旧数采，只为使用中的数采做缓存
//				collectorIdSetTemp.add(collSn);
                collectorId2devIdTemp.put(collSn, devId);
                if (dataCollector.getDeviceModelId() < 3) {
                    oldCollectorIdSetTemp.add(collSn);
                    if (!"0".equals(deviceStatus)) {
                        oldCollSnIdMapTemp.put(collSn, devId);
                    }
                } else {
                    newCollectorIdListTemp.add(collSn);
                    if (!"0".equals(deviceStatus)) {
                        newCollSnIdMapTemp.put(collSn, devId);
                    }
                }

            }
        }

        // 重新指向新缓存
//		dev2collector = dev2collectorTemp;
//		collector2devSet = collector2devSetTemp;
//		collectorIdSet = collectorIdSetTemp;
        oldCollectorIdSet = oldCollectorIdSetTemp;
        newCollectorIdList = newCollectorIdListTemp;
        collectorId2devId = collectorId2devIdTemp;
        oldCollSnIdMap = oldCollSnIdMapTemp;
        newCollSnIdMap = newCollSnIdMapTemp;

//		logger.debug("dev2collector:" + (dev2collector.toString()));
//		logger.debug("collector2devSet:" + (collector2devSet.toString()));
//		logger.debug("collectorIdSet:" + (collectorIdSet.toString()));
        logger.info("oldCollectorIdSet:" + (oldCollectorIdSet.toString()));
        logger.info("newCollectorIdList:" + (newCollectorIdList.toString()));

        logger.info("collectorId2devId:" + (collectorId2devId.toString()));
        logger.info("oldCollSnIdMap:" + (oldCollSnIdMap.toString()));
        logger.info("newCollSnIdMap:" + (newCollSnIdMap.toString()));

        //所有可用设备id,用于通讯状态判断的缓存
        List<Integer> devIdResultList = deviceMapper.selectAllDevId();
        if (devIdResultList != null) {
            //指向新集合
            devIdList = devIdResultList;
        }
        logger.info("devIdList:" + (devIdList.toString()));


        //封装下发xml所需缓存
        setPackageCache();

        //为解析所需8个缓存做准备
        dataModel2addrGuid();

        //获取解析所需的8个缓存，通过采集节点id,数据类型，pid找到所在设备id,signalGuid
        setParseCache4yc();
        setParseCache4yx();
        setParseCache4yk();
        setParseCache4yt();

        //新数采动态订阅缓存
        activeSubCache();
    }

    /**
     * @Title: setPackageCache
     * @Description: 封装下发xml所需缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月10日下午4:25:45
     */
    private void setPackageCache() {
        Map<Integer, Map<String, Integer>> devId2guidPidTemp = new HashMap<>();
        Map<String, Integer> guid2pid = new HashMap<>();

        Map<Integer, Map<String, String>> devId2guidCtrlTypeTemp = new HashMap<>();
        Map<String, String> guid2ctrlType = new HashMap<>();

        List<CollectorMessageStructure> resultList = collectorMessageStructureMapper.selectAll();
        //查询
        if (resultList != null) {

            Integer devIdTemp = null;
            Integer parseOrderTemp = null;
            Integer dataModelTemp = null;

            String collectorSn = null;
            for (CollectorMessageStructure cms : resultList) {

                //一个设备一个map
                if (guid2pid.size() != 0 || guid2ctrlType.size() != 0) {
                    guid2pid = new HashMap<>();
                    guid2ctrlType = new HashMap<>();
                }

                devIdTemp = cms.getDeviceId();
                dataModelTemp = cms.getDataModel();
                parseOrderTemp = cms.getParseOrder();
                collectorSn = cms.getCollectorSn();
                if (parseOrderTemp == null || parseOrderTemp < 0) {
                    continue;
                }


                //PID起始值
                Integer ykPidTemp = 0;
                Integer ytPidTemp = 0;
                if (parseOrderTemp == 0) {

                    //找到dataModel点表记录中遥控遥调部分
                    List<CollModelDetailMqtt> ctrlResultList = collModelDetailMqttMapper.selectAllYkYt(dataModelTemp);
                    if (ctrlResultList != null) {

                        String signalGuid = "";
                        for (CollModelDetailMqtt collModelDetailMqtt : ctrlResultList) {
                            signalGuid = collModelDetailMqtt.getSignalGuid();
                            //赋值
                            guid2pid.put(signalGuid, collModelDetailMqtt.getAddressId());
                            guid2ctrlType.put(signalGuid, collModelDetailMqtt.getSignalType());
                        }
                    }

                } else {
                    //parseOrder大于0的
                    //计算pidTemp
                    List<CollectorMessageStructure> formerParseDev = collectorMessageStructureMapper.selectFormerParseDev(collectorSn, parseOrderTemp);

                    if (formerParseDev != null) {
                        for (CollectorMessageStructure formerCms : formerParseDev) {
                            String[] sizeArr = formerCms.getSize().split(",");

                            ykPidTemp += Integer.parseInt(sizeArr[2]);
                            ytPidTemp += Integer.parseInt(sizeArr[3]);
                        }
                    }

                    //找到dataModel点表记录中遥控遥调部分
                    List<CollModelDetailMqtt> ctrlResultList = collModelDetailMqttMapper.selectAllYkYt(dataModelTemp);
                    if (ctrlResultList != null) {

                        String signalGuid = "";
                        String signalType = "";
                        for (CollModelDetailMqtt collModelDetailMqtt : ctrlResultList) {
                            signalGuid = collModelDetailMqtt.getSignalGuid();
                            signalType = collModelDetailMqtt.getSignalType();
                            //赋值
                            if ("YK".equals(signalType)) {
                                guid2pid.put(signalGuid, collModelDetailMqtt.getAddressId() + ykPidTemp);
                            } else {
                                guid2pid.put(signalGuid, collModelDetailMqtt.getAddressId() + ytPidTemp);
                            }
                            guid2ctrlType.put(signalGuid, collModelDetailMqtt.getSignalType());
                        }
                    }
                }

                if (guid2pid.size() != 0 && guid2ctrlType.size() != 0) {
                    //上一层赋值
                    devId2guidPidTemp.put(devIdTemp, guid2pid);
                    devId2guidCtrlTypeTemp.put(devIdTemp, guid2ctrlType);
                }
            }

        }
        //指向新缓存
        devId2guidPid = devId2guidPidTemp;
        devId2guidCtrlType = devId2guidCtrlTypeTemp;
        logger.info("devId2guidPid:" + (devId2guidPid.toString()));
        logger.info("devId2guidCtrlType:" + (devId2guidCtrlType.toString()));
    }

    /**
     * @Title: dataModel2addrGuid
     * @Description: 为解析所需8个缓存做准备
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月11日下午5:47:26
     */
    private void dataModel2addrGuid() {

        Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYcTemp = new HashMap<>();
        Map<Integer, List<String>> addrGuidYcTemp = null;
        List<String> guidListYcTemp = null;

        Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYxTemp = new HashMap<>();
        Map<Integer, List<String>> addrGuidYxTemp = null;
        List<String> guidListYxTemp = null;

        Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYkTemp = new HashMap<>();
        Map<Integer, List<String>> addrGuidYkTemp = null;
        List<String> guidListYkTemp = null;

        Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidYtTemp = new HashMap<>();
        Map<Integer, List<String>> addrGuidYtTemp = null;
        List<String> guidListYtTemp = null;

        List<Integer> collModelList = collModelMapper.selectAllId();
        if (collModelList != null) {
            // 按点表id遍历，//一个dataModel一个addrGuidYcTemp
            for (Integer collModelId : collModelList) {

                // ①遥测Map<dataModel, Map<addressId, List<guid>>
                dataModel2addrGuidTemp(addrGuidYcTemp, guidListYcTemp, collModelId, dataModel2addrGuidYcTemp, "YC");

                // ②遥信Map<dataModel, Map<addressId, List<guid>>>
                dataModel2addrGuidTemp(addrGuidYxTemp, guidListYxTemp, collModelId, dataModel2addrGuidYxTemp, "YX");

                // ③遥控Map<dataModel, Map<addressId, List<guid>>>
                dataModel2addrGuidTemp(addrGuidYkTemp, guidListYkTemp, collModelId, dataModel2addrGuidYkTemp, "YK");

                // ④遥调Map<dataModel, Map<addressId, List<guid>>>
                dataModel2addrGuidTemp(addrGuidYtTemp, guidListYtTemp, collModelId, dataModel2addrGuidYtTemp, "YT");
            }
        }

        // 指向新新缓存
        dataModel2addrGuidYc = dataModel2addrGuidYcTemp;
        dataModel2addrGuidYx = dataModel2addrGuidYxTemp;
        dataModel2addrGuidYk = dataModel2addrGuidYkTemp;
        dataModel2addrGuidYt = dataModel2addrGuidYtTemp;

        logger.info("dataModel2addrGuidYc:" + dataModel2addrGuidYc.toString());
        logger.info("dataModel2addrGuidYx:" + dataModel2addrGuidYx.toString());
        logger.info("dataModel2addrGuidYk:" + dataModel2addrGuidYk.toString());
        logger.info("dataModel2addrGuidYt:" + dataModel2addrGuidYt.toString());
    }

    /**
     * @param addrGuidTemp
     * @param guidListTemp
     * @param collModelId
     * @param dataModel2addrGuidTemp: void
     * @Title: dataModel2addrGuidTemp
     * @Description: dataModel2addrGuid方法内部抽取
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月18日下午6:01:13
     */
    private void dataModel2addrGuidTemp(Map<Integer, List<String>> addrGuidTemp, List<String> guidListTemp, Integer collModelId, Map<Integer, Map<Integer, List<String>>> dataModel2addrGuidTemp, String signalType) {
        addrGuidTemp = new HashMap<>();
        guidListTemp = new ArrayList<>();
        List<AddressIdGuid> addrGuidList = collModelDetailMqttMapper.selectByModelType(collModelId, signalType);
        if (addrGuidList != null) {

            Integer addrTemp = null;
            Integer addrId = null;
            //点表中分类后按地址遍历
            for (AddressIdGuid addressIdGuid : addrGuidList) {

                addrId = addressIdGuid.getAddressId();
                if (addrTemp != addrId) {

                    if (guidListTemp.size() > 0) {
                        addrGuidTemp.put(addrTemp, guidListTemp);
                        //一个addressId一个guidListYc
                        guidListTemp = new ArrayList<>();
                    }
                }
                addrTemp = addrId;
                guidListTemp.add(addressIdGuid.getSignalGuid());
            }
            //最后一个guidList存入
            if (guidListTemp.size() > 0) {
                addrGuidTemp.put(addrTemp, guidListTemp);
            }
        }
        if (addrGuidTemp.size() > 0) {
            dataModel2addrGuidTemp.put(collModelId, addrGuidTemp);
        }
    }


    /**
     * @Title: setParseCache4yc
     * @Description: 获取遥测解析所需的缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月11日下午2:42:47
     */
    private void setParseCache4yc() {
        // 遥测部分
        // 解析前两个缓存
        Map<String, Map<Integer, List<String>>> collector2pidGuidYcTemp = new HashMap<>();
        Map<Integer, List<String>> pidGuidYcTemp = null;
        List<String> guidList = null;

        Map<String, Map<Integer, Integer>> collector2pidDevIdYcTemp = new HashMap<>();
        Map<Integer, Integer> pidDevIdYcTemp = null;

        //调用通用方法封装赋值
        setParseCacheTemp("YC", 0, pidGuidYcTemp, pidDevIdYcTemp, guidList, collector2pidGuidYcTemp, collector2pidDevIdYcTemp);
        // 重新指向新缓存
        collector2pidGuidYc = collector2pidGuidYcTemp;
        collector2pidDevIdYc = collector2pidDevIdYcTemp;

        logger.info("collector2pidGuidYc:" + collector2pidGuidYc.toString());
        logger.info("collector2pidDevIdYc:" + collector2pidDevIdYc.toString());
    }


    /**
     * @Title: setParseCache4yx
     * @Description: 获取遥信解析所需的缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月11日下午2:43:08
     */
    private void setParseCache4yx() {
        // 遥信部分
        // 解析前两个缓存
        Map<String, Map<Integer, List<String>>> collector2pidGuidYxTemp = new HashMap<>();
        Map<Integer, List<String>> pidGuidYxTemp = null;
        List<String> guidList = null;

        Map<String, Map<Integer, Integer>> collector2pidDevIdYxTemp = new HashMap<>();
        Map<Integer, Integer> pidDevIdYxTemp = null;

        //调用通用方法封装赋值
        setParseCacheTemp("YX", 1, pidGuidYxTemp, pidDevIdYxTemp, guidList, collector2pidGuidYxTemp, collector2pidDevIdYxTemp);

        // 重新指向新缓存
        collector2pidGuidYx = collector2pidGuidYxTemp;
        collector2pidDevIdYx = collector2pidDevIdYxTemp;

        logger.info("collector2pidGuidYx:" + collector2pidGuidYx.toString());
        logger.info("collector2pidDevIdYx:" + collector2pidDevIdYx.toString());

    }

    /**
     * @Title: setParseCache4yk
     * @Description: 获取遥控解析所需的缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月11日下午2:43:25
     */
    private void setParseCache4yk() {
        // 遥控部分
        // 解析前两个缓存
        Map<String, Map<Integer, List<String>>> collector2pidGuidYkTemp = new HashMap<>();
        Map<Integer, List<String>> pidGuidYkTemp = null;
        List<String> guidList = null;

        Map<String, Map<Integer, Integer>> collector2pidDevIdYkTemp = new HashMap<>();
        Map<Integer, Integer> pidDevIdYkTemp = null;

        //调用通用方法封装赋值
        setParseCacheTemp("YK", 2, pidGuidYkTemp, pidDevIdYkTemp, guidList, collector2pidGuidYkTemp, collector2pidDevIdYkTemp);
        // 重新指向新缓存
        collector2pidGuidYk = collector2pidGuidYkTemp;
        collector2pidDevIdYk = collector2pidDevIdYkTemp;

        logger.info("collector2pidGuidYk:" + collector2pidGuidYk.toString());
        logger.info("collector2pidDevIdYk:" + collector2pidDevIdYk.toString());
    }

    /**
     * @Title: setParseCache4yt
     * @Description: 获取遥调解析所需的缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月11日下午2:43:39
     */
    private void setParseCache4yt() {
        // 遥调部分
        // 遥控部分
        // 解析前两个缓存
        Map<String, Map<Integer, List<String>>> collector2pidGuidYtTemp = new HashMap<>();
        Map<Integer, List<String>> pidGuidYtTemp = null;
        List<String> guidList = null;

        Map<String, Map<Integer, Integer>> collector2pidDevIdYtTemp = new HashMap<>();
        Map<Integer, Integer> pidDevIdYtTemp = null;

        //调用通用方法封装赋值
        setParseCacheTemp("YT", 3, pidGuidYtTemp, pidDevIdYtTemp, guidList, collector2pidGuidYtTemp, collector2pidDevIdYtTemp);

        // 重新指向新缓存
        collector2pidGuidYt = collector2pidGuidYtTemp;
        collector2pidDevIdYt = collector2pidDevIdYtTemp;

        logger.info("collector2pidGuidYt:" + collector2pidGuidYt.toString());
        logger.info("collector2pidDevIdYt:" + collector2pidDevIdYt.toString());

    }

    /**
     * @param typeOrder               各信号size拼接字符串中的顺序 (YC,YX,YK,YT)顺序为0,1,2,3
     * @param pidGuidTemp
     * @param pidDevIdTemp
     * @param guidList
     * @param collector2pidGuidTemp
     * @param collector2pidDevIdTemp: void
     * @Title: setParseCacheTemp
     * @Description: 获取解析所需的缓存, 抽取出来的4种信号类型通用解析缓存获取方法
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月19日上午9:50:01
     */
    private void setParseCacheTemp(String signalType, Integer typeOrder, Map<Integer, List<String>> pidGuidTemp, Map<Integer, Integer> pidDevIdTemp, List<String> guidList, Map<String, Map<Integer, List<String>>> collector2pidGuidTemp, Map<String, Map<Integer, Integer>> collector2pidDevIdTemp) {
        //在设备关联关系表中，按照采集器Sn进行分类查询
        if (oldCollectorIdSet.size() > 0) {

            for (String collSn : oldCollectorIdSet) {

                //根据parse_order 升序排列的,保证parse_order从0开始
                //每个设备pid临界值
                Integer pidCrisisTemp = 0;
                Integer pidTemp = 0;
                List<CollectorMessageStructure> collSnResultList = collectorMessageStructureMapper.selectByCollSn(collSn);
                if (collSnResultList != null) {
                    //每个collSn一个map
                    pidGuidTemp = new HashMap<>();
                    pidDevIdTemp = new HashMap<>();

                    Integer parseOrder = null;
                    String[] sizeArr = null;
                    Integer addrIdTemp = null;
                    Integer dataModel = null;

                    Integer devIdTemp = null;
                    //一个采集下所有关联设备关系遍历
                    for (CollectorMessageStructure cms : collSnResultList) {

                        parseOrder = cms.getParseOrder();
                        sizeArr = cms.getSize().split(",");
                        addrIdTemp = Integer.parseInt(sizeArr[typeOrder]);
                        dataModel = cms.getDataModel();

                        devIdTemp = cms.getDeviceId();
                        //赋值
                        pidCrisisTemp += addrIdTemp;

                        //设备有效性判断,如果关联关系无效，就不入缓存，不解析入redis
                        if ("1".equals(cms.getValid())) {
                            //pid临界值作为下一个设备的pid起始值
                            pidTemp = pidCrisisTemp;
                            continue;
                        }
                        //pid映射关系赋值
                        for (int pid = pidTemp; pid < pidCrisisTemp; pid++) {

                            //查询guid，其中addressId = pid - pidTemp
//							guidList = collModelDetailMqttMapper.selectGuidByParams(dataModel,"YC",pid - pidTemp);
                            if ("YC".equals(signalType)) {
                                guidList = dataModel2addrGuidYc.get(dataModel).get(pid - pidTemp);
                            } else if ("YX".equals(signalType)) {
                                guidList = dataModel2addrGuidYx.get(dataModel).get(pid - pidTemp);
                            } else if ("YK".equals(signalType)) {
                                guidList = dataModel2addrGuidYk.get(dataModel).get(pid - pidTemp);
                            } else if ("YT".equals(signalType)) {
                                guidList = dataModel2addrGuidYt.get(dataModel).get(pid - pidTemp);
                            }

                            if (guidList != null) {
                                //赋值
                                pidGuidTemp.put(pid, guidList);
                                guidList = new ArrayList<>();
                            }

                            pidDevIdTemp.put(pid, devIdTemp);
                        }

                        //pid临界值作为下一个设备的pid起始值
                        pidTemp = pidCrisisTemp;
                    }

                    //collSn加类型便于自己分辨
                    String collSnType = collSn + "_" + signalType;

                    //内层map赋值，collSn加类型便于自己分辨
                    if (pidGuidTemp != null) {
                        collector2pidGuidTemp.put(collSnType, pidGuidTemp);
                        pidGuidTemp = new HashMap<>();
                    }

                    if (pidDevIdTemp != null) {
                        collector2pidDevIdTemp.put(collSnType, pidDevIdTemp);
                        pidDevIdTemp = new HashMap<>();
                    }
                }
            }
        }
    }

    /*********************************************************************/

    /**
     * @Title: persistenceCacheInitial
     * @Description: 变位遥信和定时入库数据缓存实现
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月3日下午1:50:40
     */
    private void persistenceCacheInitial() {

        Map<String, Map<Integer, Integer>> signalGuid2yxIdTemp = new HashMap<String, Map<Integer, Integer>>();
        Map<Integer, Integer> signalGuid2yxIdMap = new HashMap<Integer, Integer>();

        List<Integer> yxIdAlarmListTemp = new ArrayList<Integer>();
        //含所有型号遥测遥信等的对应状态
        List<CollYxExpand> yxResult = collYxExpandMapper.selectAll();

        if (yxResult != null) {
            String signalGuidTemp = "";
            for (CollYxExpand collYxExpand : yxResult) {
                if (collYxExpand.getSignalGuid() == null) {
                    continue;
                }

                if ("重要".equals(collYxExpand.getAlarmLevel()) && "发生".equals(collYxExpand.getStatusName())) {
                    //TODO  存下严重告警的遥信id
                    yxIdAlarmListTemp.add(collYxExpand.getId());
                }

                if (signalGuidTemp.equals(collYxExpand.getSignalGuid())) {

                    signalGuid2yxIdMap.put(collYxExpand.getStatusValue(), collYxExpand.getId());

                } else {
                    // 不同的signalGuid，先存一次，第一次不存
                    if (!"".equals(signalGuidTemp)) {
                        //插入前先确认是否已有key,有就合并valueMap
                        if (signalGuid2yxIdTemp.get(signalGuidTemp) != null) {
                            Map<Integer, Integer> signalGuid2yxIdMapTemp = signalGuid2yxIdTemp.get(signalGuidTemp);
                            signalGuid2yxIdMapTemp.putAll(signalGuid2yxIdMap);
                            signalGuid2yxIdMap = signalGuid2yxIdMapTemp;
                        }
                        //数据插入缓存map
                        signalGuid2yxIdTemp.put(signalGuidTemp, signalGuid2yxIdMap);
                        // 清空会覆盖以前的引用，直接new
                        // signalGuid2yxIdMap.clear();
                        signalGuid2yxIdMap = new HashMap<Integer, Integer>();
                    }
                    // 指向新signalGuid

                    signalGuidTemp = collYxExpand.getSignalGuid();


                    signalGuid2yxIdMap.put(collYxExpand.getStatusValue(), collYxExpand.getId());
                }

            }
            // 将最后一组存进去
            signalGuid2yxIdTemp.put(signalGuidTemp, signalGuid2yxIdMap);

            // 指向新缓存
            signalGuid2yxId = signalGuid2yxIdTemp;
            logger.debug("signalGuid2yxId:" + (signalGuid2yxId.toString()));

            yxIdAlarmList = yxIdAlarmListTemp;
            logger.debug("yxIdAlarmList:" + (yxIdAlarmList.toString()));
        }

        Map<String, Map<String, String>> signalGuid2tableTemp = new HashMap<String, Map<String, String>>();

        List<FieldSignalGuidMapping> fieldMappingResult = fieldSignalGuidMappingMapper.selectAll();

        if (fieldMappingResult != null) {
            for (FieldSignalGuidMapping obj : fieldMappingResult) {

                CollSignalLabel signalLabel = collSignalLabelMapper.selectByPrimaryKey(obj.getCollId());
                if (signalLabel == null) {
                    continue;
                }
                Map<String, String> modelAttr = new HashMap<String, String>();
                modelAttr.put(Table2modelUtil.table2model(signalLabel.getTablename()), Table2modelUtil.field2Attr(signalLabel.getField()));

                signalGuid2tableTemp.put(obj.getSignalGuid(), modelAttr);

            }

            // 指向新缓存
            signalGuid2table = signalGuid2tableTemp;
            logger.info("signalGuid2table:" + (signalGuid2table.toString()));
        }

        // 这里包含数据采集器设备，遥测数据存库时，判断剔除数采设备
        Map<Integer, Integer> devId2plantIdTemp = new HashMap<Integer, Integer>();
        List<Device> deviceList = deviceMapper.selectAll();

        //TODO 新旧数采，设备和点表对应关系Map<deviceId,dataMode>
        Map<Integer, Integer> devId2dataModeMapTemp = new HashMap<Integer, Integer>(50);

        if (deviceList != null) {
            List<Integer> devJoinIdListTemp = new ArrayList<>();
            for (Device device : deviceList) {
                Integer deviceId = device.getId();
                devId2plantIdTemp.put(deviceId, device.getPlantId());
                devId2dataModeMapTemp.put(deviceId, device.getDataMode());

                //添加需要特殊处理的数采
                if ("0".equals(device.getJoinDevice())) {
                    devJoinIdListTemp.add(deviceId);
                }
            }

            // 指向新缓存
            devId2plantId = devId2plantIdTemp;
            devId2dataModeMap = devId2dataModeMapTemp;
            devJoinIdList = devJoinIdListTemp;
            logger.debug("devId2plantId:" + devId2plantId.toString());
            logger.info("devId2dataModeMap:" + devId2dataModeMap.toString());
        }

    }

    /**
     * @Title: DataProcessingCache
     * @Description: 信号点全局唯一标识signalGuid和minVal，maxVal,dataGain,correctionFactor对应关系,有符号数列表
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月8日下午4:50:24
     */
    public void dataProcessingCache() {
        Map<String, Float> signalGuid2gainTemp = new HashMap<String, Float>();
        Map<String, Float> signalGuid2factorTemp = new HashMap<String, Float>();
        Map<String, Float> signalGuid2minValTemp = new HashMap<String, Float>();
        Map<String, Float> signalGuid2maxValTemp = new HashMap<String, Float>();

        List<String> signalGuid2signedNumTemp = new ArrayList<String>();

        List<CollModelDetailMqtt> resultList = collModelDetailMqttMapper.selectAll();

        if (resultList == null) {
            return;
        }

        for (CollModelDetailMqtt collModelDetailMqtt : resultList) {

            String signalGuid = collModelDetailMqtt.getSignalGuid();

            Float dataGain = collModelDetailMqtt.getDataGain();
            Float dataFactor = collModelDetailMqtt.getCorrectionFactor();
            Float minVal = collModelDetailMqtt.getMinval();
            Float maxVal = collModelDetailMqtt.getMaxval();

            if (dataGain != null) {
                signalGuid2gainTemp.put(signalGuid, dataGain);
            }

            if (dataFactor != null) {
                signalGuid2factorTemp.put(signalGuid, dataFactor);
            }

            if (minVal != null) {
                signalGuid2minValTemp.put(signalGuid, minVal);
            }

            if (maxVal != null) {
                signalGuid2maxValTemp.put(signalGuid, maxVal);
            }
            //统计有符号数
            if (collModelDetailMqtt.getDataType() == 0) {
                signalGuid2signedNumTemp.add(signalGuid);
            }
        }

        // 指向新缓存
        signalGuid2gain = signalGuid2gainTemp;
        signalGuid2factor = signalGuid2factorTemp;
        signalGuid2minVal = signalGuid2minValTemp;
        signalGuid2maxVal = signalGuid2maxValTemp;
        signalGuid2signedNum = signalGuid2signedNumTemp;

        logger.debug("signalGuid2gain:" + (signalGuid2gain.toString()));
        logger.debug("signalGuid2factor:" + (signalGuid2factor.toString()));
        logger.info("signalGuid2minVal:" + (signalGuid2minVal.toString()));
        logger.info("signalGuid2maxVal:" + (signalGuid2maxVal.toString()));
        logger.debug("signalGuid2signedNum:" + (signalGuid2signedNum.toString()));
    }

    /**********************************************************************/
    /**
     * @Title: specialProcessCache
     * @Description: 遥测解析特殊处理缓存（多寄存器表示小数，寄存器中间几位表示遥信），实际是缓存所有点的特殊处理方法,还有单寄存器缓存
     * @lastEditor: SP0010
     * @lastEdit: 2017年11月22日下午5:39:22
     */
    public void specialProcessCache() {
        //①特殊处理方法
        Map<String, String> signalGuid2processMethodTemp = new HashMap<String, String>();
        //单寄存器缓存
        List<String> signalGuid2singleRegisterTemp = new LinkedList<String>();

        //②遥测表遥信，找到起始位和终止位(从低位向高位算),特殊遥测，只取中间几位等
        Map<String, Map<Integer, Integer>> signalGuid2processDetailTemp = new HashMap<String, Map<Integer, Integer>>();
        Map<Integer, Integer> processDetailMap = null;

        //③遥测表遥信的guid列表
        List<String> signalGuid4yc2yxTemp = new ArrayList<>();

        //④畅洋数采合成点信号高位Map<dataModel,Map<signalGuidH,signalGuidL>>
        Map<Integer, Map<String, String>> signalGuidHMapTemp = new HashMap<Integer, Map<String, String>>(50);
        //畅洋数采合成点信号低位Map<dataModel,Map<signalGuidL,signalGuidH>>
        Map<Integer, Map<String, String>> signalGuidLMapTemp = new HashMap<Integer, Map<String, String>>(50);


        List<CollModelDetailMqtt> resultList = collModelDetailMqttMapper.selectAll();
        if (resultList == null) {
            return;
        }

        String processMethodName = null;
        String signalGuid = null;
        Integer dataModel = null;
        Integer dataModelTemp = -1;
        Integer addressId = null;
        Map<String, String> guidHMapTemp = null;
        Map<String, String> guidLMapTemp = null;
        String[] strings = null;

        for (CollModelDetailMqtt collModelDetailMqtt : resultList) {
            processMethodName = collModelDetailMqtt.getSpecialProcess();
            signalGuid = collModelDetailMqtt.getSignalGuid();
            dataModelTemp = collModelDetailMqtt.getDataModel();
            addressId = collModelDetailMqtt.getAddressId();
            if (StringUtils.isNotBlank(processMethodName)) {
                signalGuid2processMethodTemp.put(signalGuid, processMethodName);
            }

            //单寄存器信号点缓存
            String singleRegister = collModelDetailMqtt.getSingleRegister();
            if (!"1".equals(singleRegister)) {
                signalGuid2singleRegisterTemp.add(signalGuid);
            }

            Integer startBit = collModelDetailMqtt.getStartBit();
            Integer bitLength = collModelDetailMqtt.getBitLength();
            if (startBit != null && bitLength != null) {
                processDetailMap = new HashMap<Integer, Integer>();
                processDetailMap.put(startBit, startBit + bitLength);
                signalGuid2processDetailTemp.put(signalGuid, processDetailMap);
            }

            //遥测表遥信
            if ("YC".equals(collModelDetailMqtt.getSignalType()) && "YX".equals(collModelDetailMqtt.getRealType())) {
                signalGuid4yc2yxTemp.add(signalGuid);
            }

            //TODO 畅洋数采合成点映射缓存，2表示多寄存器高位，3表示多寄存器低位
            if ("2".equals(singleRegister)) {
                strings = signalGuid.split("_");
                strings[3] = String.valueOf(addressId + 1);
                if (dataModel != dataModelTemp) {
                    if (dataModel != null) {
                        //保存上一个点表的
                        signalGuidHMapTemp.put(dataModel, guidHMapTemp);
                        signalGuidLMapTemp.put(dataModel, guidLMapTemp);
                    }
                    guidHMapTemp = new HashMap<String, String>(5);
                    guidLMapTemp = new HashMap<String, String>(5);
                    dataModel = dataModelTemp;
                }
                guidHMapTemp.put(signalGuid, String.join("_", strings));
                guidLMapTemp.put(String.join("_", strings), signalGuid);
            }
			/*if ("3".equals(singleRegister)) {
				strings = signalGuid.split("_");
				strings[3] = String.valueOf(addressId - 1);
				if (dataModel != dataModelTemp) {
					if (dataModel != null) {
						//保存上一个点表的
						signalGuidLMapTemp.put(dataModel, guidLMapTemp);
					}
					guidLMapTemp = new HashMap<String, String>(5);
					dataModel = dataModelTemp;
				}
				//空指针
				guidLMapTemp.put(signalGuid, String.join("_", strings));
			}*/

        }
        //保存最后一个点表高低位关系
        if (guidHMapTemp != null) {
            signalGuidHMapTemp.put(dataModel, guidHMapTemp);
        }
        if (guidLMapTemp != null) {
            signalGuidLMapTemp.put(dataModel, guidLMapTemp);
        }

        //指向新缓存
        signalGuid2processMethod = signalGuid2processMethodTemp;
        logger.debug("signalGuid2processMethod:{}", signalGuid2processMethod.toString());

        signalGuid2singleRegister = signalGuid2singleRegisterTemp;
        logger.debug("signalGuid2singleRegister:{}", signalGuid2singleRegister.toString());

        signalGuid2processDetail = signalGuid2processDetailTemp;
        logger.info("signalGuid2processDetail:{}", signalGuid2processDetail.toString());

        signalGuid4yc2yx = signalGuid4yc2yxTemp;
        logger.info("signalGuid4yc2yx:{}", signalGuid4yc2yx.toString());

        signalGuidHMap = signalGuidHMapTemp;
        signalGuidLMap = signalGuidLMapTemp;
        logger.info("signalGuidHMap:{}", signalGuidHMap.toString());
        logger.info("signalGuidLMap:{}", signalGuidLMap.toString());
    }

    /**
     * @Title: sunRiseSetCache
     * @Description: 设备所在地日出日落时间缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月3日下午2:11:43
     */
    public void sunRiseSetCache() {
        Map<Integer, Long[]> devId2SunRiseSetTemp = new HashMap<Integer, Long[]>();
        List<CollDeviceLocation> deviceLocationList = deviceMapper.selectDeviceLocation();
        //当前时间
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (deviceLocationList != null) {
            for (CollDeviceLocation cdl : deviceLocationList) {
                String location = cdl.getLocation();
                if (StringUtils.isNotBlank(location)) {
                    //存放今日日出日落时间数组
                    Long[] riseSetArr = new Long[2];
                    String[] locationArr = location.split(",");
                    try {
                        String sunrise = SunRiseSet.getSunrise(Double.parseDouble(locationArr[0]), Double.parseDouble(locationArr[1]), cal, 8);
                        String sunset = SunRiseSet.getSunset(Double.parseDouble(locationArr[0]), Double.parseDouble(locationArr[1]), cal, 8);
                        //日出日落前后一个小时有效
                        riseSetArr[0] = sdf.parse(sunrise).getTime() - 1 * 60 * 60 * 1000;
                        riseSetArr[1] = sdf.parse(sunset).getTime() + 1 * 60 * 60 * 1000;
                        logger.debug(Arrays.toString(riseSetArr));
                        //封装缓存
                        devId2SunRiseSetTemp.put(cdl.getId(), riseSetArr);
                    } catch (ParseException e) {
                        logger.error("parse sunRiseSetStr error:{}", e);
                    }
                }
            }
            //指向新缓存
            devId2SunRiseSet = devId2SunRiseSetTemp;
            logger.debug("devId2SunRiseSet:{}", devId2SunRiseSet.toString());
        }
    }

    /**
     * @Title: exampleDeviceCache
     * @Description: 示例电站相关设备缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年1月19日下午2:20:52
     */
    private void exampleDeviceCache() {
        List<Integer> exampleCollListTemp = new ArrayList<Integer>();

        List<Integer> exampleInverterListTemp = new ArrayList<Integer>();

        exampleCollListTemp = deviceMapper.selectExampleColl();
        exampleInverterListTemp = deviceMapper.selectExampleInverter();
        if (exampleCollListTemp != null) {
            exampleCollList = exampleCollListTemp;
        }
        if (exampleInverterListTemp != null) {
            exampleInverterList = exampleInverterListTemp;
        }

        logger.debug("exampleCollListTemp:{}", exampleCollListTemp.toString());
        logger.debug("exampleInverterListTemp:{}", exampleInverterListTemp.toString());
    }

    /**
     * @Title: activeSubCache
     * @Description: 新数采动态订阅缓存实现
     * @lastEditor: SP0010
     * @lastEdit: 2018年3月30日下午6:26:32
     */
    public void activeSubCache() {
        //生成新数采动态订阅的的相关缓存
        List<String[]> subTopicListTemp = new ArrayList<String[]>(5);
        List<int[]> subQosListTemp = new ArrayList<int[]>(5);
        Map<String, Integer> collSn2ClientIdxTemp = new HashMap<String, Integer>(5);

        double quotient = newCollectorIdList.size() * 1.0d / SynConstant.MAXSUBDEVSIZE;
        //需要的满订阅连接数
        int integerCount = (int) quotient;
        //需要的连接数
        int needCount = (int) Math.ceil(quotient);
        newClientCount = needCount;
        //非饱和订阅连接中管理机数
        int remainder = newCollectorIdList.size() % SynConstant.MAXSUBDEVSIZE;

        String[] topicArr = null;
        int[] qosArr = null;
        String collSnTemp = null;
        //记录collsn对应client索引
        int idxCount = -1;
        for (int i = 0; i < newCollectorIdList.size(); i++) {

            if (needCount < 1) {
                break;
            }

            int index = i % SynConstant.MAXSUBDEVSIZE;

            if (index == 0) {
                //保存上一个连接订阅信息
                if (i != 0) {
                    subTopicListTemp.add(topicArr);
                    subQosListTemp.add(qosArr);
                }

                if (i / SynConstant.MAXSUBDEVSIZE < integerCount) {
                    topicArr = new String[SynConstant.MAXSUBDEVSIZE];
                    qosArr = new int[SynConstant.MAXSUBDEVSIZE];
                    idxCount++;
                } else {
                    topicArr = new String[remainder];
                    qosArr = new int[remainder];
                    idxCount++;
                }

            }
            collSnTemp = newCollectorIdList.get(i);
            topicArr[index] = "syn_" + collSnTemp + "_pub";
            //qosArr[index] = 0;//默认就是0

            collSn2ClientIdxTemp.put(collSnTemp, idxCount);
        }
        //保存最后一个连接订阅信息
        subTopicListTemp.add(topicArr);
        subQosListTemp.add(qosArr);

        //重新指向和日志
        subTopicList = subTopicListTemp;
        subQosList = subQosListTemp;
        collSn2ClientIdx = collSn2ClientIdxTemp;

        logger.info("subTopicList:{}", subTopicList.toString());
        logger.info("subQosList:{}", subQosListTemp.toString());
        logger.info("collSn2ClientIdx:{}", collSn2ClientIdx.toString());
    }

    /**
     * @Title: newCollParseCache
     * @Description: TODO 新数采新数采解析缓存
     * @lastEditor: SP0010
     * @lastEdit: 2018年4月16日下午5:31:48
     */
    public void newCollParseCache() {
        //Map<coll_id,Map<comId_slaveId, devId>>
        Map<Long, Map<String, Long>> slaveId2devIdMapTemp = new HashMap<Long, Map<String, Long>>(50);
        //Map<devId,coll_id_comId_slaveId>
        Map<Long, String> devId2slaveIdMapTemp = new HashMap<Long, String>(50);

        List<CollJsonDevSet> selectGroupByCollId = collJsonDevSetMapper.selectOrderByCollId();
        if (selectGroupByCollId != null) {
            Long collId = 0L;
            Long collIdTemp = -1L;
            Map<String, Long> slaveIdMapTemp = null;

            String comSlave = null;
            for (CollJsonDevSet collJsonDevSet : selectGroupByCollId) {
                collIdTemp = collJsonDevSet.getCollId();
                if (collIdTemp != collId) {
                    //提交上一个数采的
                    if (collId != 0L) {
                        slaveId2devIdMapTemp.put(collId, slaveIdMapTemp);
                    }
                    slaveIdMapTemp = new HashMap<String, Long>(2);
                    collId = collIdTemp;
                }
                comSlave = collJsonDevSet.getComId() + "_" + collJsonDevSet.getSlaveId();
                slaveIdMapTemp.put(comSlave, collJsonDevSet.getDevId());
                devId2slaveIdMapTemp.put(collJsonDevSet.getDevId(), collJsonDevSet.getCollId() + "_" + comSlave);
            }
            if (slaveIdMapTemp != null) {
                slaveId2devIdMapTemp.put(collId, slaveIdMapTemp);
            }
        }
        //指向新内存
        slaveId2devIdMap = slaveId2devIdMapTemp;
        devId2slaveIdMap = devId2slaveIdMapTemp;

        logger.info("slaveId2devIdMap:{}", slaveId2devIdMap.toString());
        logger.info("devId2slaveIdMap:{}", devId2slaveIdMap.toString());

        //TODO  新数采解析缓存Map<dataModel, Map<addressId, List<guid>>
        Map<Integer, Map<String, List<String>>> dataModel2addrGuidTemp = new HashMap<Integer, Map<String, List<String>>>(50);
        //TODO 新数采合成点高位地址Map<dataModel,List<addrH>>
        Map<Integer, List<String>> addrHighListMapTemp = new HashMap<Integer, List<String>>(50);

        //TODO 新数采地址Map<dataModel,Set<addr + 03/06.. + 1/0>>，只包含遥测遥信
        Map<Integer, Set<String>> addrSetMapTemp = new HashMap<Integer, Set<String>>(50);

        //TODO 新数采下发控制指令缓存，查找功能码，只包含遥控遥调
        Map<String, String> guidCodeMapTemp = new HashMap<String, String>(50);

        //TODO guid和高位地址缓存Map<signalGuid,addrHigh>
        Map<String, String> guidAddrHighMapTemp = new HashMap<String, String>();

        List<CollJsonModelDetailExtend> selectGroupByModel = collJsonMDExtMapper.selectOrderByModel();
        if (selectGroupByModel != null) {
            Integer modelId = null;
            Integer modelIdTemp = -1;
            Map<String, List<String>> addrGuidMapTemp = null;
            List<String> guidListTemp = null;
            List<String> addrHListTemp = null;
            //TODO 应数采要求，地址去重
            Set<String> addrSetTemp = null;
            String addrTemp = "";
            String addr = null;
            //地址读取方式
            String addrRead = null;
            //地址数组
            String[] strings = null;
            //guid,临时
            String guidTemp = null;

            for (CollJsonModelDetailExtend collJsonMDExtend : selectGroupByModel) {
                modelIdTemp = collJsonMDExtend.getDataModel();
                if (modelIdTemp != modelId) {
                    //提交上一个数采的
                    if (modelId != null) {
                        dataModel2addrGuidTemp.put(modelId, addrGuidMapTemp);
                        addrHighListMapTemp.put(modelId, addrHListTemp);
                        addrSetMapTemp.put(modelId, addrSetTemp);
                    }
                    addrGuidMapTemp = new HashMap<String, List<String>>(50);
                    modelId = modelIdTemp;
                    addrHListTemp = new ArrayList<String>();
                    //TODO 应数采要求，从小到大排序,且地址去重
                    addrSetTemp = new TreeSet<String>(new AddressComparator());
                }

                addrRead = collJsonMDExtend.getFunCode() + collJsonMDExtend.getDynamic();

                addrTemp = collJsonMDExtend.getRegAddress();
                strings = addrTemp.split(",");
                addrTemp = strings[0];

                //TODO 判断Guid中遥控遥调标志
                guidTemp = collJsonMDExtend.getSignalGuid();
                if (guidTemp.contains("YK") || guidTemp.contains("YT")) {
                    guidCodeMapTemp.put(guidTemp, collJsonMDExtend.getFunCode());
                    //TODO 只用在遥控遥调
                    guidAddrHighMapTemp.put(guidTemp, addrTemp);

                } else {
                    addrSetTemp.add(addrTemp + addrRead);

                    //TODO 多寄存器处理,高位地址存起来,低位的 guidList加进去
                    if (strings.length > 1) {
                        addrHListTemp.add(addrTemp);
                        addrSetTemp.add(strings[1] + addrRead);
                        //TODO guidAddrHighMapTemp.put(guidTemp, addrTemp);

                        //加入低位guidList
                        if (!strings[1].equals(addr)) {
                            if (addr != null) {
                                addrGuidMapTemp.put(addr, guidListTemp);
                            }
                            guidListTemp = new ArrayList<String>();
                            addr = strings[1];
                        }
                        //TODO 依赖其他缓存找到低位guid
                        guidListTemp.add(CacheMappingUtil.getSignalGuidL(modelIdTemp, guidTemp));
                    }
                }

                //处理高位或普通电guidList
                if (!addrTemp.equals(addr)) {
                    if (addr != null) {
                        addrGuidMapTemp.put(addr, guidListTemp);
                    }
                    guidListTemp = new ArrayList<String>();
                    addr = addrTemp;
                }
                guidListTemp.add(guidTemp);
            }

            //最后一个点表存入
            if (guidListTemp != null) {
                addrGuidMapTemp.put(addr, guidListTemp);
            }
            if (addrGuidMapTemp != null) {
                dataModel2addrGuidTemp.put(modelId, addrGuidMapTemp);
            }
            //最后一个点表存入高位地址map
            if (addrHListTemp != null) {
                addrHighListMapTemp.put(modelId, addrHListTemp);
            }
            //最后一个点表存入地址map
            if (addrSetTemp != null) {
                addrSetMapTemp.put(modelId, addrSetTemp);
            }

        }

        //指向新内存
        dataModel2addrGuid = dataModel2addrGuidTemp;
        addrHighListMap = addrHighListMapTemp;
        addrSetMap = addrSetMapTemp;
        guidAddrHighMap = guidAddrHighMapTemp;
        guidCodeMap = guidCodeMapTemp;

        logger.info("dataModel2addrGuid:{}", dataModel2addrGuid.toString());
        logger.info("addrHighListMap:{}", addrHighListMap.toString());
        logger.info("addrSetMap:{}", addrSetMap.toString());
        logger.info("guidAddrHighMap:{}", guidAddrHighMap.toString());
        logger.info("guidCodeMap:{}", guidCodeMap.toString());

    }

}
