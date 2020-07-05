package services.interfaces;

import handlers.Language;
import java.util.List;

public interface IBotService {

    List<String> fetchCategoriesByParentId(Long id, Language language);

    List<String> fetchCategoriesByParentCategoryName(String parentCatName, Language language);

    List<String> fetchQuestionsByCategoryId(Long id, Language language);

    List<String> fetchQuestionsByCategoryName(String categoryName, Language language);

    String fetchAnswerByQuestion(String question, Language language);
}
