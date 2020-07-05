package services;

import com.vdurmont.emoji.EmojiParser;
import handlers.Language;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import services.interfaces.IBotService;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BotService implements IBotService {

    private static final String BASEURLQUESTIONS = "http://localhost:9966/api/v1/questions/";
    private static final String BASEURLCATEGORIES = "http://localhost:9966/api/v1/categories/";
    private static volatile BotService instance;

    private BotService(){

    }

    public static BotService getInstance(){
        BotService currentInstance;
        if(instance == null){
            synchronized (BotService.class){
                if(instance == null){
                    instance = new BotService();
                }
                currentInstance = instance;
            }
        }
        else{
            currentInstance = instance;
        }
        return currentInstance;
    }

    @Override
    public List<String> fetchCategoriesByParentId(Long id, Language language){
        List<String> responseToUser = new ArrayList<>();
        try{
            String URL = BASEURLCATEGORIES + language.getLanguage() + "/" + id;
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(
                    new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(URL);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf,"UTF-8");
            if (responseString.isEmpty()) throw new Exception();
            JSONArray jArray = (JSONArray) new JSONTokener(responseString).nextValue();
            for(int i = 0;i<jArray.length();i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String str = jObject.getString("categoryName");
                responseToUser.add(str);
            }
        } catch (Exception e){
        }
        return responseToUser;
    }

    @Override
    public List<String> fetchCategoriesByParentCategoryName(String parentCatName, Language language){
        List<String> responseToUser = new ArrayList<>();
        try{
            String URL = BASEURLCATEGORIES + language.getLanguage() + "/cat?category="
                    + URLEncoder.encode(parentCatName, "UTF-8");
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(
                    new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(URL);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf,"UTF-8");
            if (responseString.isEmpty()) throw new Exception();
            JSONArray jArray = (JSONArray) new JSONTokener(responseString).nextValue();
            for(int i = 0;i<jArray.length();i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String str = jObject.getString("categoryName");
                responseToUser.add(str);
            }
        } catch (Exception e){
        }
        return responseToUser;
    }

    @Override
    public List<String> fetchQuestionsByCategoryId(Long id, Language language){
        List<String> responseToUser = new ArrayList<>();
        try{
            String URL = BASEURLQUESTIONS + "lang/" + language.getLanguage() + "/cat/" + id;
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(
                    new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(URL);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf,"UTF-8");
            JSONArray jArray = (JSONArray) new JSONTokener(responseString).nextValue();
            for(int i = 0;i<jArray.length();i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String str = jObject.getString("question");
                responseToUser.add(str);
            }
        } catch (Exception e){
        }
        return responseToUser;
    }

    @Override
    public List<String> fetchQuestionsByCategoryName(String categoryName, Language language){
        List<String> responseToUser = new ArrayList<>();
        try{
            String URL = BASEURLQUESTIONS + "lang/" + language.getLanguage() + "/catname?category="
                    + URLEncoder.encode(categoryName, "UTF-8");
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(
                    new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(URL);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf,"UTF-8");
            JSONArray jArray = (JSONArray) new JSONTokener(responseString).nextValue();
            for(int i = 0;i<jArray.length();i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String str = jObject.getString("question");
                responseToUser.add(str);
            }
        } catch (Exception e){
        }
        return responseToUser;
    }

    @Override
    public String fetchAnswerByQuestion(String question,Language language){
        String responseToUser;
        try{

            String URL = BASEURLQUESTIONS + "lang/" + language.getLanguage() + "/question?question="
                    + URLEncoder.encode(question, "UTF-8");
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(
                    new NoopHostnameVerifier()).build();

            HttpGet request = new HttpGet(URL);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);

            String responseString = EntityUtils.toString(buf,"UTF-8");
            JSONObject jsonObject = new JSONObject(responseString);


            responseToUser = jsonObject.getString("answer");

        } catch (Exception e){
            if (language == Language.KAZ)
                return EmojiParser.parseToUnicode("Мәзірден опцияны таңдаңыз:arrow_down:");
            else
                return EmojiParser.parseToUnicode("Выберите нужную опцию из меню:arrow_down:");
        }
        return responseToUser;
    }
}
