package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import java.util.List;

public class NewsBean implements Serializable {

    /**
     * return_code : 200
     * return_msg : success
     * return_data : {"top_list":[],"com_list":[{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5MDk1NzQzMQ==&mid=2653269655&idx=3&sn=3da5be2d486df4740226c287b8e1df1a&chksm=bd6de48f8a1a6d990b066bf1c720ee247d29879b8d32cca6127109fdf493753efab2553da368&scene=0","nid":"54517","signs":"","show_type":"1","chaid":"0","title":"中国空间站时代来了","fromid":"24","pubtime":1538186965,"author":"环球时报","show_img":["http://newsimg.xy599.com/15148478115bac804533a342.46669699.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MTQzMjE1NjQwMQ==&mid=2655549937&idx=1&sn=6a51609f12f5ae6c4567e2c5223032b7&chksm=66dfd66f51a85f792442f99c02df85d72762f2bbef0fc219b4021a2301f71c7c0968a514743e&scene=0","nid":"54526","signs":"","show_type":"1","chaid":"0","title":"你还要继续用脸刷开手机吗？","fromid":"24","pubtime":1538184585,"author":"虎嗅网","show_img":["http://newsimg.xy599.com/14975238355bac804f3db671.39077941.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzU2NTQzMjQ2NA==&mid=2247493080&idx=2&sn=b2aee7794a54f45ff6dd6c158f01b59b&chksm=fcb97c27cbcef53120cf2f7ee66654de1da19c1b4ea3516748785be83f9636683805495d626b&scene=0","nid":"54530","signs":"","show_type":"1","chaid":"0","title":"月薪5万的女人，私生活都是什么样的？","fromid":"24","pubtime":1538179803,"author":"咪蒙","show_img":["http://newsimg.xy599.com/15037897295bac8053d87bc8.34897745.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MTQzMjE1NjQwMQ==&mid=2655549937&idx=2&sn=81f896e6e32a6b17eaba53bd52c27583&chksm=66dfd66f51a85f79419f44ed7230237b3bd89179686a8c53a6cf45d750b8f1e47feb8c283389&scene=0","nid":"54523","signs":"","show_type":"1","chaid":"0","title":"iPhone 基带之困","fromid":"24","pubtime":1538173530,"author":"虎嗅网","show_img":["http://newsimg.xy599.com/17028660435bac804bc80936.11786523.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5ODAwMzA0MA==&mid=2648721732&idx=3&sn=b2ab5d3263bc70af2161d4f3b786a4b4&chksm=bec5814b89b2085dc26077bc3c39e081159994ab31e8ee20ea0e9b5e65f789891819d1eb2e4f&scene=0","nid":"54416","signs":"","show_type":"1","chaid":"0","title":"10大动图：为什么要带我出来！一起感受下二哈的生无可恋","fromid":"24","pubtime":1538172680,"author":"糗事百科","show_img":["http://newsimg.xy599.com/3906505735bac7fd0567904.40573466.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5MDk1NzQzMQ==&mid=2653269655&idx=1&sn=73e59974d13cb91900158f4be57ebaa5&chksm=bd6de48f8a1a6d99cb1c3a7d3b84218e5103d74cd21c1604679afa255ac51054330665be1331&scene=0","nid":"54518","signs":"","show_type":"1","chaid":"0","title":"这两天，中国四面楚歌？","fromid":"24","pubtime":1538172253,"author":"环球时报","show_img":["http://newsimg.xy599.com/21463689105bac80464adb56.58244042.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzI0NDYwNDU4Ng==&mid=2247517638&idx=2&sn=cb398960a44d0ced426a5c152253e72b&chksm=e9598e03de2e071591bd521ba56e986382be7cdb5d9054fd3bff2ffb869ea83e765e713f5cb6&scene=0","nid":"54438","signs":"","show_type":"1","chaid":"0","title":"什么是女人的终极安全感？（30-50岁必看）","fromid":"24","pubtime":1538171589,"author":"知书先生","show_img":["http://newsimg.xy599.com/3103541325bac7fe9669374.43281721.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzU3NTQ0MDc2Ng==&mid=2247506515&idx=2&sn=e354925af0e9f7fd674ac69cfc443e27&chksm=fd218842ca560154cdb7fcd183f6eda22629b1c023b8bfb0d3da8a06525cb0bceda224a62aea&scene=0","nid":"54522","signs":"","show_type":"1","chaid":"0","title":"我才25岁，怎么慌成这样？","fromid":"24","pubtime":1538170949,"author":"HUGO","show_img":["http://newsimg.xy599.com/5230415505bac804aaf3819.85234659.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5MDk1NzQzMQ==&mid=2653269655&idx=4&sn=803d6c14827546c4e65dd5ec876fc1e1&chksm=bd6de48f8a1a6d99607729a0a26977ece271d3901872e2b8aa1fcf58c7b65cb450269b8cfdd1&scene=0","nid":"54520","signs":"","show_type":"1","chaid":"0","title":"\u201c快手迪丽热巴\u201d火了！背后推手竟是一名80后扶贫书记","fromid":"24","pubtime":1538170670,"author":"环球时报","show_img":["http://newsimg.xy599.com/9637339425bac80487e4111.88440879.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzU2NTQzMjQ2NA==&mid=2247493080&idx=1&sn=2b45b35fe154a79f2030e053ca2b796c&chksm=fcb97c27cbcef531d3c97b041511ac7a3a5db50f8418092265751db1c64b5f52959b2dd29aa6&scene=0","nid":"54529","signs":"","show_type":"1","chaid":"0","title":"和张雨绮一样敢爱敢恨的女生，最后怎么样了？","fromid":"24","pubtime":1538170663,"author":"咪蒙","show_img":["http://newsimg.xy599.com/14015367635bac8052a54199.91378355.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5MDk1NzQzMQ==&mid=2653269655&idx=2&sn=980132c2f28694a771c3c5117db838b4&chksm=bd6de48f8a1a6d9952c3fc4d576ce4cf0b5befbf60f20c037b0abc7f48b8f7a9fecd5a312d87&scene=0","nid":"54519","signs":"","show_type":"1","chaid":"0","title":"韩国首次提出这种要求，就看日本的反应了","fromid":"24","pubtime":1538169188,"author":"环球时报","show_img":["http://newsimg.xy599.com/18515961025bac8047622fa8.44867323.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MTQzMjE1NjQwMQ==&mid=2655549937&idx=3&sn=b03395558d86c75386fbd74d561f9e2a&chksm=66dfd66f51a85f798fc5536e1fa69d29e285f493074c471ec9da17d7be6c5d39995078cb4298&scene=0","nid":"54525","signs":"","show_type":"1","chaid":"0","title":"【虎嗅晚报】凤凰网被停更整改；海底捞首日股价退潮","fromid":"24","pubtime":1538167966,"author":"虎嗅网","show_img":["http://newsimg.xy599.com/1805303335bac804e26a568.58124580.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzA4NDI3NjcyNA==&mid=2649411805&idx=1&sn=0ad5e59e0d3f8e10c920c10eccf3a24b&chksm=87f7d446b0805d5016d59c67fa37d7bf3bdc18b2c7b90133707e353165b66ac9922ff289024d&scene=0","nid":"54527","signs":"","show_type":"1","chaid":"1","title":"夜读 | 不经历低谷的彷徨，就没有翱翔的渴望","fromid":"24","pubtime":1538165729,"author":"新华社","show_img":["http://newsimg.xy599.com/19661462925bac80505d9029.31490790.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzI0NDYwNDU4Ng==&mid=2247517638&idx=5&sn=0db919292df55e4812ce8b633c5be999&chksm=e9598e03de2e07150b376d221dcdf512ed392100f19221f3d20438f47c72d5bfe44f1460a91e&scene=0","nid":"54439","signs":"","show_type":"1","chaid":"0","title":"这3种迹象，暗示你的生活正慢慢变好","fromid":"24","pubtime":1538164889,"author":"知书先生","show_img":["http://newsimg.xy599.com/1827529355bac7fea767f46.21052387.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzI0NDYwNDU4Ng==&mid=2247517638&idx=6&sn=65d5edb883d023d4be5fb4fb5cb4b11e&chksm=e9598e03de2e0715e9ad011bbd101f559c78872216bce112e0afdb7ece49829dac3c494eecde&scene=0","nid":"54435","signs":"","show_type":"1","chaid":"0","title":"睡前思考一段话","fromid":"24","pubtime":1538161795,"author":"知书先生","show_img":["http://newsimg.xy599.com/19175993985bac7fe61c5172.55196888.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzI0NDYwNDU4Ng==&mid=2247517638&idx=3&sn=f0e4c120ea337211f4119b96c30f3f4b&chksm=e9598e03de2e071563e189c986ee31eda9f53a05474d277af063424661382dc2eccc49201734&scene=0","nid":"54436","signs":"","show_type":"1","chaid":"0","title":"请尊重孩子的磨蹭，90%的父母不知道的秘密","fromid":"24","pubtime":1538161479,"author":"知书先生","show_img":["http://newsimg.xy599.com/10538294445bac7fe7329013.34359276.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MTQzMjE1NjQwMQ==&mid=2655549937&idx=4&sn=65e3b52e73d2f84afa7808fd54249e6d&chksm=66dfd66f51a85f79510f4f6ad2b69ad7e390dcab7d65afc6e96853b9c3354fcac498e3eb5277&scene=0","nid":"54524","signs":"","show_type":"1","chaid":"0","title":"明尼苏达往事","fromid":"24","pubtime":1538160217,"author":"虎嗅网","show_img":["http://newsimg.xy599.com/20489527705bac804d06aae2.55255495.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5MTMwOTU4MA==&mid=2654782195&idx=3&sn=ecc7e54cc227049388318eb21c09f69c&chksm=bd7fe8908a08618602c892f74dbbb298e813e017ec023983e15b1f712dc9a71ba8b066e1ee86&scene=0","nid":"54443","signs":"","show_type":"1","chaid":"0","title":"比遭遇家暴更可怕的，是求助遇到和稀泥","fromid":"24","pubtime":1538156306,"author":"新闻哥","show_img":["http://newsimg.xy599.com/11577772265bac7fef482e41.69068650.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MzI3MTA2MTk4NQ==&mid=2650587178&idx=1&sn=d3a0314bd28015a79411e4ddc65d8452&chksm=f2cfe6a4c5b86fb25a52cc3bfa353656153b5c641fa5837fa9dd628f107a00e80785033996d9&scene=0","nid":"54528","signs":"","show_type":"1","chaid":"0","title":"建议全国学习安溪\u201c五个一律\u201d，借力扫黑除恶彻底整治电信诈骗！","fromid":"24","pubtime":1538151822,"author":"终结诈骗","show_img":["http://newsimg.xy599.com/5941782015bac805177c579.76605990.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]},{"h5url":"http://mp.weixin.qq.com/s?__biz=MjM5MTMwOTU4MA==&mid=2654782195&idx=1&sn=91925bd11e032ba10124813ea51c5db6&chksm=bd7fe8908a086186e881d29dc846a0faf2babb981ecc0ba15dfe9bff64a50d2a0038ce79fc53&scene=0","nid":"54444","signs":"","show_type":"1","chaid":"0","title":"贾樟柯，一位被电影耽误的掐架大师","fromid":"24","pubtime":1538150523,"author":"新闻哥","show_img":["http://newsimg.xy599.com/16017575195bac7ff057cb12.20084843.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]}]}
     */

    private String return_code;
    private String return_msg;
    private ReturnDataBean return_data;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public ReturnDataBean getReturn_data() {
        return return_data;
    }

    public void setReturn_data(ReturnDataBean return_data) {
        this.return_data = return_data;
    }

    public static class ReturnDataBean {
        private List<?> top_list;
        private List<ComListBean> com_list;

        public List<?> getTop_list() {
            return top_list;
        }

        public void setTop_list(List<?> top_list) {
            this.top_list = top_list;
        }

        public List<ComListBean> getCom_list() {
            return com_list;
        }

        public void setCom_list(List<ComListBean> com_list) {
            this.com_list = com_list;
        }

        public static class ComListBean {
            /**
             * h5url : http://mp.weixin.qq.com/s?__biz=MjM5MDk1NzQzMQ==&mid=2653269655&idx=3&sn=3da5be2d486df4740226c287b8e1df1a&chksm=bd6de48f8a1a6d990b066bf1c720ee247d29879b8d32cca6127109fdf493753efab2553da368&scene=0
             * nid : 54517
             * signs :
             * show_type : 1
             * chaid : 0
             * title : 中国空间站时代来了
             * fromid : 24
             * pubtime : 1538186965
             * author : 环球时报
             * show_img : ["http://newsimg.xy599.com/15148478115bac804533a342.46669699.jpg?imageView2/1/w/312/h/240/format/jpg/interlace/1"]
             */

            private String h5url;
            private String nid;
            private String signs;
            private String show_type;
            private String chaid;
            private String title;
            private String fromid;
            private int pubtime;
            private String author;
            private List<String> show_img;

            public String getH5url() {
                return h5url;
            }

            public void setH5url(String h5url) {
                this.h5url = h5url;
            }

            public String getNid() {
                return nid;
            }

            public void setNid(String nid) {
                this.nid = nid;
            }

            public String getSigns() {
                return signs;
            }

            public void setSigns(String signs) {
                this.signs = signs;
            }

            public String getShow_type() {
                return show_type;
            }

            public void setShow_type(String show_type) {
                this.show_type = show_type;
            }

            public String getChaid() {
                return chaid;
            }

            public void setChaid(String chaid) {
                this.chaid = chaid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFromid() {
                return fromid;
            }

            public void setFromid(String fromid) {
                this.fromid = fromid;
            }

            public int getPubtime() {
                return pubtime;
            }

            public void setPubtime(int pubtime) {
                this.pubtime = pubtime;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public List<String> getShow_img() {
                return show_img;
            }

            public void setShow_img(List<String> show_img) {
                this.show_img = show_img;
            }
        }
    }
}
