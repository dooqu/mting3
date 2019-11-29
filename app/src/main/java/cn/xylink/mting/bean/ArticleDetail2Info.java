package cn.xylink.mting.bean;

/**
 * @author wjn
 * @date 2019/11/28
 */
public class ArticleDetail2Info {

    /**
     * articleId : 2019102211541422454428823
     * title : 一套完整设计案例，如何在3天内设计出一款APP？
     * url : https://mp.weixin.qq.com/s/VYq2kY0BsrU7_jIbnHKPXQ
     * picture :
     * content : 全文共3275词，阅读大约需要9分钟，记得点击上面的 蓝字 关注我哟！
     很多人说看过很多设计道理却依然做不好设计，还不如直接看设计案例来的简单粗暴。今天为大家找到一篇实战好文，一个人，3天时间，打造一款APP全流程设计，学起来吧！
     之前我还翻译过另一篇日本设计大神的设计案例 一个UX案例研究—Foursquare，也同样推荐阅读。
     几周前，我接到一个设计需求是为食品行业设计一套解决方案，甲方给出的时间是必须在3天内完成。乍一听，可能会觉得这几乎是不可能的，但是如果你熟悉“GV Design Sprint ”（译者注：GV Design Sprint就是一个专业的设计流程方案，想了解的朋友请见https://designsprintkit.withgoogle.com/introduction/overview），那么就会明白，这实际上是可以做到的。
     我发现这是一个非常好的机会，可以在这么短的时间内实践一套设计方法。在这篇文章中，我将逐一向大家分解我的设计过程和每天的设计进展。
     项目背景
     Common Food是一个使用社区支持农业(CSA)出售水果蔬菜的农场。人们需要在年初提前预定农场一年的收成，然后在生长季节里，社区成员每周都会收到一箱食品。（译者注：CSA的运作过程通常由认同相同理念的社区支持者（个人或单位）采用共同购买的模式，预先支付一笔费用给当地的小农户预约一季或一年的收成，农民收成后，再根据实际产出的多寡分配给会员，如此达到结合社区力量支持在地农民采用有机农耕的积极意义。——百度百科）
     设计挑战
     设计一款应用，帮助普通农场每周能向会员们卖出更多的农副产品。帮助他们个性化的推荐食品方案，以满足会员们不同的知识水平，兴趣和饮食限制。
     第一天：资料收集
     研究方法
     研究阶段往往会占用一些时间，所以尽可能的利用手头现有信息是节省时间的好办法。市场环境二手资料，竞争对手，用户调研或者直接去App Store中收集用户评论，我需要尽早了解清楚用户的思维模式。
     用户调研
     Common Food 公司会在每个季度对会员们进行回访，以便能更好的了解他们的喜好，以下是今年的一些反馈：
     “我喜欢烹饪和做罐头。我可以多买一些西红柿吗？” “大头菜我不喜欢吃，我把它都扔掉了，不过生菜确实很好吃。” “我儿子对花椰菜过敏，所以我们从不吃花椰菜。另外我喜欢吃蒜蓉，但不知道怎么做。” “我希望我能有更多的大头菜，然后用来做泡菜。” “ 我觉得我们应该需要更多的CSAs，继续扩大农场的规模。我现在看到了很多转基因的怪物农作物，希望有渠道能及时发布最新消息。” “我们能不能弄到更多的甜菜？” “我从来没听过白菜，它看起来很漂亮，但是我不知道怎么做这道菜。” 一份来自Field Agent的最新报告发现，不管是在计划内还是计划外购物的网购者来说，农产品都是一个很受欢迎的类别。65%的受访消费者表示，他们购买的是新鲜农产品，与冷冻乳制品并列第一。31%的人说他们一时冲动买了新鲜水果和蔬菜，超过了零食（23%）和糖果（14%）。然而，39%的网购者并不会在网上购买新鲜农产品，因为他们更喜欢去菜市场。
     市场调研
     市场研究公司Mintel发现，尽管消费者越来越多的转向网上购物，但只有十分之一的美国人会通过电商购买新鲜的农产品，肉类，家禽和鱼类。该研究建议，为了打消消费者的顾虑，提高销售额，零售商应该多提倡节约成本，并提供更全面的产品信息，以建立信任，提升价值吸引力。
     研究表明，亚马逊的“购物车和收藏”等功能对于忙碌的消费者来说也是一个很有吸引力的点，特别是女性（48%）比男性（37%）更有可能在线购买，做好功能体验很重要。
     头脑风暴
     脑暴出的问题
     1、我们如何收集用户需求数据来减少浪费和分配食品？
     2、我们如何通过季节性食物和促销活动来增加销售业绩？
     3、我们如何帮助会员根据他们的饮食偏好发掘额外的食物需求？
     用户画像
     用户调查的结果提供了足够的信息来创建用户画像。我选择创建与年龄和性别都无关的角色，以便能够将更加聚焦在如何平等的实现用户目标上。
     用户画像
     竞品分析
     当我准备进入草图阶段时，我将会研究在相关行业或竞争对手那里类似的问题和解决方案，以确定最佳方案。
     盒子大小和内容分类
     FarmBox Direct和Farm Fresh to you两款产品分别提供不同的盒子大小和产品选项。（译者注：为什么会定义盒子大小，这是因为他们每周会发放给会员的食品是以盒子来计量的。）
     自定义盒子装的食品种类
     FarmFresh to You让你定制你的盒子，添加或删除农产品和设置数量。但你必须充会员才能定制它，与此同时，你也可以为排除项创建项目列表。
     FarmFresh to You
     真实的食品照片
     Farmstead 允许添加任意数量的购物清单，你可以添加，删除和浏览，都没问题之后再来结账。Farmstead提供真实新鲜的产品照片，不像其他竞争对手使用库存的照片。
     方案思考
     我的解决方案是设计一个APP，从会员那里收集数据，比如家庭规模、饮食偏好等等，让会员们可以轻松定制季节性食谱。利用人工智能了解会员的饮食习惯，提供食谱，饮食建议和运营一些有针对性的促销活动，取得双方共赢。这些数据可以帮助我们从会员那里得到反馈，然后就可以知道他们在哪个季节可能会重新订购哪种类型的产品或组合，以及他们实际上最喜欢什么产品。
     第二天：草图和设计
     我喜欢在自己感觉“明白了”之后开始画草图，我会把自己的想法都画出来，然后再把自己觉得不好的想法剔除掉，保留自己觉得最好的效果。我会通过绘制用户旅程地图来定义用户任务和目标。
     用户旅程地图
     解决了用户角色，竞品分析和草图方案，接下来，我就开始为注册了这些服务的会员们设计流程。一旦他们通过APP首次下单，我们的数据库中就有了用户的日程安排、发货和账单信息。一旦确认了这些流程信息，我就准备开始画线框图。
     线框图
     线框图是APP的骨架，这让我在开始做视觉设计之前，能够专注于关键功能、元素和交互。我选择了高保真线框图，这样我只需要在一些色彩，配图，和图标上进一步思考视觉呈现。
     风格板
     在画好线框图之后，我会找相同行业内的APP视觉做一个风格板来作为设计参考。
     配色方案
     我选择了鲜红色作为主色。鲜艳的颜色会让人胃口大开，红色也会引发购物欲望（促销、清仓、热闹等氛围）。
     字体选择
     图标设计
     当说到图标和节省时间这个话题时，我不建议把它们都画出来。尽管我很喜欢自己画图标，但是这次时间上并不允许。我推荐一些图标库，比如Material Design icons （https://material.io/tools/icons/），或者我这个应用用到的一个图标库The Noun Project （https://thenounproject.com/#）。在利用这些图标库时，请注意购买版权，如果不想付费，也必须注明图标来源，定稿后有时间再来重新画。
     Logo设计
     我期望这个APP能有一个清晰易懂的名字，同时也希望能有一个与名字非常匹配的Logo。在思考了很多名字之后，我觉得“FameCrate(农场条板箱)”这个名字非常的适合，通过不断迭代，优化，最终得到了下面这个Logo。
     视觉设计
     我喜欢尝试不同的风格和设计变化，我对第一稿不满意，因为缺乏整体上的一致性，然后不断优化，直到最终全局页面都能做到统一协调并符合自己的设计预期为止。
     第三天：交互原型
     到了第三天，我完成了最后的视觉设计，便开始做可交互原型。我等到这个阶段才开始做这事，主要是因为如果在线框阶段就开始做原型的话，尽管它们是高保真的（我经常这么做），但会花很多时间在交互界面上。对于这个项目，我在设计完成后再来添加交互动作，其实是为了节省时间。
     最终设计
     自动动画
     我会用Adobe XD的自动动画（https://helpx.adobe.com/xd/help/create-prototypes-using-auto-animate.html）功能来做原型设计，这是节省交互动画制作时间的好办法，而不是在After Effects上花费几个小时来做这种效果。
     下一步
     可用性测试
     找一些真实用户来做这项测试。我个人最喜欢用Maze.Design（https://maze.design/）这款工具来记录测试结果，它易于使用，并能提供有关原型的全面数据分析。
     不断迭代
     通过可用性测试的一些结论，在设计上进行迭代，改进用户流程。
     总结
     在有限的时间内，你绝对不能让自己分心，必须专注于这个项目并管理好自己的时间。我会给自己进行计时，并在每一个步骤上给自己设定时间限制。我试着让APP用起来尽可能的简单，从草图和用户旅程地图就要开始思考，最后这一切才能水到渠成。
     感悟
     永远相信你的直觉，不要害怕设计修改。如果你把我的线框图和最终的设计效果图进行比较，会发现我做了很多的选择来改进最终的设计。另外，也可以使用你自己熟悉的软件，或者你自己知道的更快速的方法，而不要太花费时间来确定是不是跟我完全一样。
     原文：https://uxdesign.cc/ux-ui-case-study-designing-a-food-app-in-3-days-1e2856680205
     作者：Paola Ascanio
     译者：彩云Sky
     本文翻译已获得作者的正式授权
     授权截图
     往期热门文章回顾
     2019开工利是，第一份设计文章合集
     [穿越福城] 幕后故事 | 设计定义年味
     从360到腾讯设计师，我的2018年度总结
     从设计师转岗到产品经理的一些思考，6个意想不到的工作差异
     2019年UI和UX设计趋势
     Ps:新号没有留言功能，喜欢的话就点个好看吧，谢谢~
     * userId : 1
     * nickName : 轩辕联
     * sourceName : null
     * store : 0
     * createAt : 1571716454224
     * updateAt : 1571716454224
     * progress : 0
     * shareUrl : http://test.xylink.cn/article/2019102211541422454428823
     * inType : 3
     * checked : 0
     */

    private String articleId;
    private String title;
    private String url;
    private String picture;
    private String content;
    private String userId;
    private String nickName;
    private Object sourceName;
    private int store;
    private long createAt;
    private long updateAt;
    private int progress;
    private String shareUrl;
    private int inType;
    private int checked;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Object getSourceName() {
        return sourceName;
    }

    public void setSourceName(Object sourceName) {
        this.sourceName = sourceName;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
