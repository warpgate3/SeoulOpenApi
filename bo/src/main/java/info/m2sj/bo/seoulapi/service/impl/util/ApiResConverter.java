package info.m2sj.bo.seoulapi.service.impl.util;

import info.m2sj.bo.core.exceptions.BaseException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * API 응답 문자열 변환 Util
 * XML -> JSON
 * String -> XML Document
 */
public final class ApiResConverter {
    private ApiResConverter() {}

    public static Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new BaseException("Exception occurs during string to xml");
        }
    }

    public static JSONObject xmlDocumentToJson(NodeList nodeList) {
        JSONArray jsonArray = xmlDocumentToJsonArray(nodeList);
        return jsonArray.getJSONObject(0);
    }

    public static JSONArray xmlDocumentToJsonArray(NodeList nodeList) {
        JSONArray dataArr = new JSONArray();
        JSONObject dataObject = new JSONObject();
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.hasChildNodes() && tempNode.getChildNodes().getLength() > 1) {
                    JSONArray temArr = xmlDocumentToJsonArray(tempNode.getChildNodes());
                    if (dataObject.containsKey(tempNode.getNodeName())) {
                        dataObject.getJSONArray(tempNode.getNodeName()).add(temArr.getJSONObject(0));
                    } else {
                        dataObject.put(tempNode.getNodeName(), temArr);
                    }
                } else {
                    dataObject.put(tempNode.getNodeName(), tempNode.getTextContent());
                }
            }
        }
        dataArr.add(dataObject);
        return dataArr;
    }
}
