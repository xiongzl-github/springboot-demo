package top.timebook.xdonlineeducationproject.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * @ClassName WXPayutil
 * @Description 微信工具类
 * @Author xiongzl
 * @Date 2019/5/10 15:00
 * @Version 1.0
 **/
public class WXPayUtil {

    /**
     * @Author xiongzl
     * @Description XML格式字符串转换为Map
     * @Date 2019/5/14 15:34
     * @Param [strXML]
     * @Return java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        Map<String, String> data = new HashMap<>(10);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        } catch (Exception ex) {
            // do nothing
        }
        return data;
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    /**
     * @Author xiongzl
     * @Description 生成微信支付sign
     * @Date 2019/5/10 17:31
     * @Param [params, key]
     * @Return java.lang.String
     **/
    public static String createSign(SortedMap<String, String> params, String key) {
        if (null != params && !params.isEmpty() && null != key) {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, String>> es = params.entrySet();
            //生成 stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA";
            for (Map.Entry<String, String> entry : es) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k).append("=").append(v).append("&");
                }
            }
            sb.append("key=").append(key);
            System.out.println("str: ");
            System.out.println(sb.toString());
            return Objects.requireNonNull(CommonUtils.MD5(sb.toString())).toUpperCase();
        } else {
            return null;
        }
    }


    /**
     * @Author xiongzl
     * @Description 校验签名
     * @Date 2019/5/10 17:38
     * @Param [params, key]
     * @Return boolean
     **/
    public static boolean isCorrectSign(SortedMap<String, String> params, String key) {
        String sign = createSign(params, key);
        String wxPaySign = params.get("sign").toUpperCase();
        return wxPaySign.equals(sign);
    }

    /**
     * @Author xiongzl
     * @Description 接收微信返回的参数
     * @Date 2019/5/14 15:29
     * @Param [request]
     * @Return java.lang.String
     **/
    public static String getMessage(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder sb = null;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (null == sb) {
                sb = new StringBuilder(line);
            } else {
                sb.append(line);
            }
        }
        bufferedReader.close();
        return Objects.requireNonNull(sb).toString();
    }


    /**
     * @Author xiongzl
     * @Description 将普通map转换为SortedMap
     * @Date 2019/5/14 21:04
     * @Param [map]
     * @Return java.util.SortedMap<java.lang.String,java.lang.String>
     **/
    public static SortedMap<String, String> tranSortedMap(Map<String, String> map){
        SortedMap<String, String> sortedMap = null;
        for (String key : map.keySet()) {
            String value = map.get(key);
            String temp;
            if (null != value) {
                if (null == sortedMap) {
                    sortedMap = new TreeMap<>();
                }
                temp = value.trim();
                sortedMap.put(key, temp);
            }

        }
        return sortedMap;
    }


    /**
     * @Author xiongzl
     * @Description 封装响应给微信的信息
     * @Date 2019/5/15 18:14
     * @Param [response, code]
     * @Return void
     **/
    public static void responseResultInfo(HttpServletResponse response, int code) throws IOException {
        response.setContentType("text/xml");
        if (code == 0) {
            response.getWriter().println("fail");
        }else {
            response.getWriter().println("success");
        }
    }
}
