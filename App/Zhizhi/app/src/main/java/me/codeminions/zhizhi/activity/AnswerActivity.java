package me.codeminions.zhizhi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnticipateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import java.io.IOException;
import java.lang.ref.WeakReference;

import me.codeminions.zhizhi.R;
import me.codeminions.zhizhi.bean.Answer;
import me.codeminions.zhizhi.net.HttpUtil;
import me.codeminions.zhizhi.tools.Constant;
import me.codeminions.zhizhi.tools.MHandler;
import me.codeminions.zhizhi.view.RLScrollView;
import me.codeminions.zhizhi.view.UI;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;


public class AnswerActivity extends AppCompatActivity {

    public static void actionStart(Context context, Answer answer) {
        Intent intent = new Intent(context, AnswerActivity.class);
        intent.putExtra("an", answer);
        context.startActivity(intent);
    }

    WebView web;
    TextView titleView;
    TextView nameView;
    TextView desView;
    RLScrollView scrollView;
    RelativeLayout bottom;
    Answer answer;
    String content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerweb);

        answer = (Answer) getIntent().getSerializableExtra("an");
        initWeight();
        initData();

        // 支持javascript
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgReset();
            }
        });

        resquestContent();

    }

    void initData(){
        titleView.setText(answer.getQuestion());
        nameView.setText(answer.getAuthor());
        desView.setText(answer.getAuthor_des());
    }

    void initWeight(){
        titleView = findViewById(R.id.txt_question_tit);
        nameView = findViewById(R.id.txt_Name);
        desView = findViewById(R.id.txt_des);
        scrollView = findViewById(R.id.scroll);
        web = findViewById(R.id.web);
        bottom = findViewById(R.id.view_bottom);

        scrollView.setOnScrollListener(new RLScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldxX, int oldY) {

                if(Math.abs(y-oldY) < 20)
                    return ;

                float transY = 0;
//                Toast.makeText(AnswerActivity.this, "滑动..", Toast.LENGTH_SHORT).show();
                if(oldY < y) {
                    transY = UI.dipToPx(getResources(), 48);
                }
                bottom.animate()
                        .translationY(transY)
                        .setInterpolator(new AnticipateInterpolator(1 ))
                        .setDuration(320)
                        .start();
            }
        });
    }


    void resquestContent() {

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.LOW) {

            String url = "/" + answer.getQuestionId() + "/" + answer.getAnswerId();
            @Override
            public void run() {
                HttpUtil.sendHttpRequest(Constant.URL_BASE + Constant.URL_ANSWER, new FormBody.Builder()
                        .add("con", url)
                        .build(), new HttpUtil.MyCallback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        content = response.body().string();
                        handler.sendEmptyMessage(1);
                    }
                });
            }
        }, ThreadType.NORMAL_THREAD);
    }

    MHandler handler = new MHandler(this, new MHandler.HandleCallBack() {
        @Override
        public void handleMessage(Message msg, WeakReference reference) {
            web.loadDataWithBaseURL(null, content.replaceAll("<*noscript>", ""), "text/html", "UTF-8", null);
        }
    });


    /**
     * 重置webview中img标签的图片大小
     */
    private void imgReset() {
        web.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    // String sss = "<span class=\"RichText ztext CopyrightRichText-richText\" itemprop=\"text\">这个必答。藏了多年的句子，终于有个地方可以分享了。<br><br>偶开天眼觑红尘，可怜身是眼中人。——《浣西沙·山寺微茫》王国维<br>………………………………………………8.20凌晨更 时间真快 也要快上大学了<br><br><br><br>移舟去。未成新句，一砚梨花雨。<br>——《点绛唇·访牟存叟南漪钓隐》周晋<br>…………………………………………………………4.19晚更。今天上语文课看到的句子。高考倒计时49天了。<br><br><br>而浮生若梦，为欢几何?<br><br>——《春夜宴从弟桃花园序》李白<br>……………………………………………………………3月22日语文连堂更【既然如此，珍惜好当前的快乐吧<br><br><br>可怜无定河边骨，犹是春闺梦里人。<br><br>——《陇西行四首其二》陈陶<br><br>问人生，到此凄凉否？<br>千万恨，<br>为君剖。<br><br>——《金缕曲》顾贞观<br><br>梦里不知身是客，<br>一晌贪欢。<br><br>——《浪淘沙》李煜<br><br>君埋泉下泥销骨，我寄人间雪满头。<br><br>——《梦微之》白居易<br><br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉10.31晚更。嗯。大家要加油。要坚持。要争取。要懂得珍惜。<br><br>浮云一别后，流水十年间。<br><br>——韦应物《 淮上喜会梁川故人 》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉10.28晚更。<br><br>锦城虽云乐，不如早还家。<br><br>——李白《蜀道难》<br><br>欲买桂花同载酒，<br>终不似，少年游。<br><br>——刘过《唐多令》<br><br>欲把情怀输写尽，<br>终不似，少年游。<br><br>—— 刘学箕《唐多令》<br><br><br>才是别离情便苦，<br>都莫问，淡和浓。<br><br>——张林《唐多令》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉偶然看到一首唐多令，有点喜欢它的格律。好想知道这个词牌名是怎么来的，可是查不到。<br><br><br>春风无限潇湘意，欲采苹花不自由。<br><br>——柳宗元《酬曹侍御过象县见寄》<br><br>一蓑烟雨任平生。<br><br>——苏轼《定风波》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉以上10.4早更。春风二句是在故宫的书店里翻一本叫《诗词拔萃》的书看到的。喜爱。<br><br>绿蚁新醅酒，红泥小火炉。<br>晚来天欲雪，能饮一杯无。<br><br>——白居易《问刘十九》<br><br>行到水穷处，坐看云起时。<br><br>——王维《终南别业》<br><br>一愿郎君千岁，<br>二愿妾身常健，<br>三愿如同梁上燕，<br>岁岁长相见。<br><br>——冯延巳《长命女》<br><br>天下三分明月夜，二分无赖是扬州。<br><br>——徐凝《忆扬州》<br><br>遗民泪尽胡尘里，南望王师又一年。<br><br>——陆游《 秋夜将晓出篱门迎凉有感 》<br><br>力拔山兮气盖世。时不利兮骓不逝。<br>骓不逝兮可奈何，虞兮虞兮奈若何。<br><br>——项羽《垓下歌》<br><br>我居北海君南海，寄雁传书谢不能。<br>桃李春风一杯酒，江湖夜雨十年灯。<br><br>——黄庭坚《寄黄几复》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉9.28晚更。辛弃疾的丑奴儿读起来挺有感觉的。<br><br><br><br><br>年年岁岁花相似，岁岁年年人不同。<br><br>——刘希夷《代悲白头翁》<br><br>还君明珠双泪垂，恨不相逢未嫁时。<br><br>——张籍《节妇吟》<br><br>玲珑骰子安红豆，入骨相思知不知。<br><br>——温庭筠《南歌子》<br><br>终日两相思。<br>为君憔悴尽，<br>百花时。<br><br>——温庭筠《南歌子》<br><br>动愁吟，<br>碧落黄泉，<br>两处难寻。<br><br>——朱彝尊《高阳台》<br><br>自埋剑履歌尘散，红袖香销已十年。<br><br>——张仲素《燕子楼》<br><br>酒入愁肠，<br>化作相思泪。<br><br>——范仲淹《苏幕遮》<br><br>此情无计可消除，<br>才下眉头，<br>却上心头。<br><br>——李清照《一剪梅》<br><br>而今独自睚昏黄。<br>行也思量。<br>坐也思量。<br>锦字都来三两行。<br>千断人肠。<br>万断人肠。<br><br>——辛弃疾《一剪梅》<br><br>曾经沧海难为水，除却巫山不是云。<br><br>——元稹《离思》<br><br>一川烟草，<br>满城风絮。<br>梅子黄时雨。<br>——贺铸《青玉案》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉以上9.25晚更。嗯。诗词都是一个人的日子。<br><br>刘郎已恨蓬山远，更隔蓬山一万重。<br><br>——李商隐《无题》<br><br>可怜夜半虚前席，不问苍生问鬼神。<br><br>——李商隐《贾生》<br><br>春心莫共花争发，一寸相思一寸灰。<br><br>——李商隐《无题》<br><br>此情可待成追忆，只是当时已惘然。<br><br>——李商隐《锦瑟》<br><br>故人早晚上高台，<br>寄我江南春色一枝梅。<br><br>——舒亶《虞美人》<br><br>八骏日行三万里，穆王何事不重来。<br><br>——李商隐《瑶池》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉以上9.23晚更，李商隐李商隐。刘郎已恨蓬山远，更隔蓬山一万重。<br><br><br><br>和我，免使年少光阴虚过。<br><br> ——柳永《定风波》<br><br><br>年年今夜，月华如练，长是人千里。…<br>都来此事，眉间心上，无计相回避。<br><br> ——范仲淹《御街行》<br><br>伤情处，<br>高城望断，<br>灯火已黄昏。<br><br> ——秦观《满庭芳》<br><br>断送一生憔悴，只消几个黄昏。<br><br> ——赵令畤《清平乐》<br><br>歌筵畔，<br>先安簟枕，<br>容我醉时眠。<br><br> ——周邦彦《满庭芳》【尤其喜爱 容我醉时眠 这句，南宋李好古也曾写： 有酒更如海，容我醉时眠。 <br><br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉以上9.22晚更.今晚被数学所困 更得少…<br><br><br>沙上并禽池上暝，云破月来花弄影。<br><br> ——张先《天仙子》<br><br>心似双丝网，中有千千结。<br><br> ——张先《千秋岁》<br><br>满目山河空念远，落花风雨更伤春。<br>不如怜取眼前人。<br><br> ——晏殊《浣溪沙》<br><br>当时共我赏花人，点检如今无一半。<br><br> ——晏殊《木兰花》<br><br>无穷官柳，无情画舸，无根行客。…<br>刘郎鬓如此，况桃花颜色。<br><br> ——晁补之《忆少年》<br><br>旧香残粉似当初。<br>人情恨不如。 <br><br> ——晏几道《阮郎归》<br><br>杏花疏影里，吹笛到天明。<br><br> ——陈与义《临江仙》<br><br>此后锦书休寄，画楼云雨无凭。<br><br> ——晏几道《清平乐》<br>拟歌先敛，<br>欲笑还颦，<br>最断人肠。<br><br> ——欧阳修《诉衷情》<br><br>泪眼问花花不语，乱红飞过秋千去。<br><br> ——欧阳修《蝶恋花》<br><br>春色三分，<br>二分尘土，<br>一分流水。<br>细看来，<br>不是杨花，<br>点点是离人泪。<br><br> ——苏轼《水龙吟》<br>﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉<br>以上9.21更新【说不定会一直更新。因为日子还长，触碰人心的句子，还有很多。<br><br><br>叹人间，美中不足今方信。<br>纵然是齐眉举案，到底意难平。<br><br> ——曹雪芹《终身误》<br><br>此去经年，<br>应是良辰好景虚设。<br>便纵有千种风情，<br>更与何人说？<br><br> ——柳永《雨霖铃》<br><br>不忍登高临远，<br>望故乡渺邈，<br>归思难收。<br><br> ——柳永《八声甘州》<br><br><br>若教眼底无离恨，不信人间有白头。<br><br> ——辛弃疾《鹧鸪天》<br><br>世间无限丹青手，一片伤心画不成。<br><br> ——高蟾《金陵晚望》<br><br>妾拟将身嫁与一生休。<br>纵被无情弃，<br>不能羞。<br><br> ——韦庄《思帝乡》<br><br>天涯地角有穷时，只有相思无尽处。<br><br> ——晏殊《玉楼春》<br><br>夜月一帘幽梦，春风十里柔情。<br><br> ——秦观《八六子》<br><br>当时明月在，曾照彩云归。<br><br> ——晏几道《临江仙》<br><br>今宵剩把银釭照，犹恐相逢是梦中。<br><br> ——晏几道《鹧鸪天》<br><br>春梦秋云，聚散真容易。<br><br> ——晏几道《蝶恋花》<br><br>赌书消得泼茶香，当时只道是寻常。<br><br> —— 纳兰容若《浣溪沙》<br><br><br><br>…………………………………………………………………………<br>待更新待更新【好句子只消一句一句看，我这样都集中到一起，反而觉得有些惭愧。</span>";


}
