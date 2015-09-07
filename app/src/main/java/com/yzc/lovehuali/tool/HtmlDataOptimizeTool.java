package com.yzc.lovehuali.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/2/2 0002.
 */
public class HtmlDataOptimizeTool {
    public Document HtmlDataOptimizeTool(String htmlData) {

        String thumbHtmlData = htmlData;//定义一个压缩图片版的数据
        thumbHtmlData.replaceAll(".jpg","_thumb.jpg");//对数据里面带有图片地址的后缀名，替换成缩略图的后缀名
        //这是针对特定情况的，下面是通用的样式处理


        thumbHtmlData = thumbHtmlData.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;","");//替换9个空格或以上

        /*Pattern pattern = Pattern.compile("<BR>+&nbsp;{2,}");使用正则表达式去到多余的空行，这里的表达式不对
        Matcher matcher = pattern.matcher(thumbHtmlData);
        thumbHtmlData = matcher.replaceAll("");*/

        Document document =  Jsoup.parseBodyFragment(thumbHtmlData);//将网页数据的body片段转成文档

        //定制处理规则
        document.select("IMG").removeAttr("style");
        document.select("IMG").attr("style","max-width:100%");
        //document.select("IMG").attr("width","100%");
        //document.select("IMG").attr("height","auto");
        document.select("FONT").attr("size","3");

        Element head = document.select("head").first();
        head.prepend("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\"/>\n");

        return document;
    }
}
