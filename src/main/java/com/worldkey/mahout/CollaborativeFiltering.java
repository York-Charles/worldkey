package com.worldkey.mahout;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingUserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 协同过滤算法封装类
 * @author HP
 */
@Component
@ConfigurationProperties(prefix = "mahout")
public class CollaborativeFiltering {
    private Logger log= LoggerFactory.getLogger(CollaborativeFiltering.class);
    private DataModel model = null;
    @Resource
    private DruidDataSource dataSource;
    public CollaborativeFiltering() {
        super();
    }


    private void config() throws TasteException {
        JDBCDataModel jDBCDataModel = new MySQLJDBCDataModel(dataSource, "browsing_history", "user", "item", "pf", "time");
        model = new ReloadFromJDBCDataModel(jDBCDataModel);
    }


    /**
     * 基于User的推荐算法，集合七种算法的推荐结果
     *
     * @param userID  推荐的用户ID
     * @param howMany 推荐的数量，每种算法推荐的数量
     * @return 推荐的物品的Set集合
     * @throws Exception 比较懒的人都会抛出Exception异常
     */
    public List<Long> baseUser(Long userID, Integer howMany) throws Exception {
        if (this.getModel() == null) {
            this.config();
            log.info("初始化");
        } else {
            model.refresh(null);
        }
        ArrayList<UserSimilarity> userSimilarities = new ArrayList<>();
        //余弦相关度算法
        //userSimilarities.add(new UncenteredCosineSimilarity(model));
        //曼哈顿距离 不需要评分
        // userSimilarities.add(new CityBlockSimilarity(model));
        //皮尔逊系数相关度  适合计算用户的相似度
        userSimilarities.add(new PearsonCorrelationSimilarity(model));
        //欧式距离相似度
        //userSimilarities.add(new EuclideanDistanceSimilarity(model));
        //对数似然相似度 不需要评分
        // userSimilarities.add(new LogLikelihoodSimilarity(model));
        //Spearman秩相关系数
        //userSimilarities.add(new SpearmanCorrelationSimilarity(model));
        //Tanimoto系数
        // userSimilarities.add(new TanimotoCoefficientSimilarity(model));

        List<RecommendedItem> recommendedItems = new ArrayList<>();
        ArrayList<Long> longs = new ArrayList<>();
        userSimilarities.forEach(e -> {
            try {
                CachingUserSimilarity cachingUserSimilarity = new CachingUserSimilarity(e, model);
                UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, cachingUserSimilarity, model);
                Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, cachingUserSimilarity);
                recommendedItems.addAll(recommender.recommend(userID, howMany));

                recommendedItems.forEach(item -> longs.add(item.getItemID()));
            } catch (TasteException e1) {
                e1.printStackTrace();
            }
        });
        return longs;
    }

    public DataModel getModel() {
        return model;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }


}
