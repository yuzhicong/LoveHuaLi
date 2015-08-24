package com.yzc.lovehuali.tool;


import com.yzc.lovehuali.R;

/**
 * Created by Administrator on 2015/2/1 0001.
 */
public class getLogoResourceFromName {

    int LogoResource;
    public int getLogoResourceFromName(String clubName) {

        String ClubNamelist[]={"华立社联","院记者团","电子协会","蓝天义工协会","音乐协会","书画协会","羽毛球协会","公共关系协会",
        "灵韵DV话剧社","ERP学习社","土木建筑协会","计算机协会","心理协会","篮球协会","MC魔艺协会","跆拳道协会",
        "机械工程协会","橄榄球协会","会计协会","英语俱乐部","创业交流协会","就业促进协会","商务交流协会",
        "RED HOT舞团","放飞文学社","摄影协会","炎之动漫社","飞轮协会","金融协会","读者协会","天文协会",
        "乒乓球协会","足球协会","市场营销协会","华立棋社","记忆协会","辩论协会","排球协会","创艺DIY协会",
        "精武协会","演讲与口才协会","网球协会","毽球协会"};

        int LogoResourcelist[]={R.drawable.clublogo_sau,R.drawable.clublogo_jzt,R.drawable.clublogo_dzxh,
        R.drawable.clublogo_bluesky,R.drawable.clublogo_yyxh,R.drawable.clublogo_shxh,R.drawable.clublogo_hualiyx,
        R.drawable.clublogo_gggxxh,R.drawable.clublogo_lyjs,R.drawable.clublogo_erpxxs,R.drawable.clublogo_tmjzxh,
        R.drawable.clublogo_computerxh,R.drawable.clublogo_xlxh,R.drawable.clublogo_lqxh,R.drawable.clublogo_mc_magic,
        R.drawable.clublogo_hltqdxh,R.drawable.clublogo_jxsjxh,R.drawable.clublogo_hlglqxh,R.drawable.clublogo_hjxh,
        R.drawable.clublogo_englishxh,R.drawable.clublogo_cyjlxh,R.drawable.clublogo_jycjxh,R.drawable.clublogo_swjlxh,
        R.drawable.clublogo_red_hot,R.drawable.clublogo_ffwxs,R.drawable.clublogo_photoxh,R.drawable.clublogo_yzdms,
        R.drawable.clublogo_rollerxh,R.drawable.clublogo_finance,R.drawable.clublogo_readerxh,R.drawable.clublogo_twxh,
        R.drawable.clublogo_pingpongxh,R.drawable.clublogo_footballxh,R.drawable.clublogo_scyxxh,R.drawable.clublogo_chess,
        R.drawable.clublogo_jyxh,R.drawable.clublogo_blxh,R.drawable.clublogo_pqxh,R.drawable.clublogo_cydiyxh,
        R.drawable.clublogo_jwxh,R.drawable.clublogo_yjykcxh,R.drawable.clublogo_wqxh,R.drawable.clublogo_jqxh};

        for(int i=0;i<ClubNamelist.length;i++){
            if (ClubNamelist[i].equals(clubName)) {
                LogoResource = LogoResourcelist[i];
                return LogoResource;
            }
        }
        return 0;//根据协会名称，匹配对应的logo资源值
    }
}
