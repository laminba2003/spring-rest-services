package com.spring.training;

import com.spring.training.util.LoggingAspect;
import com.spring.training.domain.Country;
import com.spring.training.domain.Person;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

@Import({TestSecurityConfig.class, AopAutoConfiguration.class, LoggingAspect.class})
public abstract class BaseTestClass {

    protected Person getPerson() {
        return new Person(1L, "Mamadou Lamine", "Ba", getCountry());
    }

    protected Country getCountry() {
        return new Country("France", "Paris", 1223333677);
    }

    protected String getToken() {
        return "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlUktVWG10TFhKMHBBNkxBS29aWko1ZlU0VDhCdmxKdERCb3pXanFFdnhjIn0.eyJleHAiOjE2NDg2MDUzNjYsImlhdCI6MTY0ODU2OTM2NywiYXV0aF90aW1lIjoxNjQ4NTY5MzY2LCJqdGkiOiI2OWI5Y2Q2Ny1hZDZiLTQzNzAtOTNlZC1iOWQyZWZlOGMzYTQiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODMvYXV0aC9yZWFsbXMvZXhhbXBsZSIsInN1YiI6ImE1NDYxNDcwLTMzZWItNGIyZC04MmQ0LWIwNDg0ZTk2YWQ3ZiIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFjY291bnQiLCJzZXNzaW9uX3N0YXRlIjoiOGIxMmQxNDYtNmNkYy00MDY0LTk3MzQtZjkwYzNhNDZlYWE4IiwiYWNyIjoiMSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJjYnNDbGllbnRJZCI6IjM0NDM0MzQzNDQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwidGVzdCI6IjU1NjY2NTU2Iiwicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LWNvbnNlbnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsIm1hbmFnZS1jb25zZW50IiwiQURNSU4iLCJ1c2VyIiwidmlldy1wcm9maWxlIl0sIm5hbWUiOiJKb2huIFRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huQHRlc3QuY29tIiwiZ2l2ZW5fbmFtZSI6IkpvaG4iLCJmYW1pbHlfbmFtZSI6IlRlc3QiLCJlbWFpbCI6ImpvaG5AdGVzdC5jb20ifQ.Pgca2mWrVY9h_e5czB_Xeen3cle6BDzJlIY5BDHoAtbdic69P-VSIwWKYFAyH9DqaQSW3nTWCJa9arhftF2J_LaOpjvJA-mT0BryUsXX3xM5AFEEydhpV4GyDexCZzaaZCjXw2215IiLQmHaOkwRyqakKOzGJJLdYTTQpcbgfyl89aAiRe90HEm_XCJzvawfjvoUrfpaonCSoduizIlBj9PorTK0w5CJH460xG6bl7FrCI6xBZ_uAqOCs1yt7b7RvaCYxunZ1ylgvRWy4w7yeaCElfgOa76IXqeM9C6o5EsuQdJuTIa_1qDcUj9T6hNelIw2KQfrAjsj38Fim4bzNw";
    }

    protected MessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
