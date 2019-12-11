package com.filochowski.crawlerbackend.scrapper.service;


import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.AnnotateTextRequest.Features;
import com.google.cloud.language.v1.AnnotateTextResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleAPISerivce {

  /**
   * Detects entities,sentiment and syntax in a document using the Natural Language API.
   */
  public static void main(String[] args) throws Exception {
    String text = "Biden wrote: Did you see the video of our friends and allies in London this week? World leaders were LAUGHING at the President of the United States, after he once again embarrassed himself and tarnished the reputation of the United States at a summit.";
    analyzeEntitiesText(text);
//    analyzeSentimentText(text);
//    analyzeSyntaxText(text);
//    classifyText(text);
//    entitySentimentText(text);
//    annotateText(text);
  }

  /**
   * Identifies entities in the string {@code text}.
   */
  public static void analyzeEntitiesText(String text) throws Exception {
    // [START language_entities_text]
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16)
          .build();

      AnalyzeEntitiesResponse response = language.analyzeEntities(request);

      // Print the response
      for (Entity entity : response.getEntitiesList()) {
        System.out.println("====================================================");
        System.out.printf("Entity: %s", entity.getName());
        System.out.printf("\nSalience: %.3f\n", entity.getSalience());
        System.out.println("Metadata: ");
        System.out.println("-----------------------");

        for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
          System.out.printf("\n%s : %s", entry.getKey(), entry.getValue() + "\n");
        }
        System.out.println("Mentions: ");
        System.out.println("-----------------------");
        for (EntityMention mention : entity.getMentionsList()) {
          System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
          System.out.printf("Content: %s\n", mention.getText().getContent());
          System.out.printf("Type: %s\n\n", mention.getType());
        }
      }
    }
    // [END language_entities_text]
  }

  /**
   * Identifies the sentiment in the string {@code text}.
   */
  public Sentiment analyzeSentimentText(String text) throws Exception {
    // [START language_sentiment_text]
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
      Sentiment sentiment = response.getDocumentSentiment();
      if (sentiment == null) {
        System.out.println("No sentiment found");
      } else {
        System.out.printf("Sentiment magnitude: %.3f\n", sentiment.getMagnitude());
        System.out.printf("Sentiment score: %.3f\n", sentiment.getScore());
      }
      return sentiment;
    }
    // [END language_sentiment_text]
  }

  public AnnotateTextResponse annotateText(String text) {
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      log.info("Google Api text input length: " + text.length());
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      Features features = Features.newBuilder()
          .setExtractSyntax(true)
          .setExtractEntities(true)
          .setExtractDocumentSentiment(true)
          .setExtractEntitySentiment(true)
          .setClassifyText(true)
          .build();
      return language.annotateText(doc, features);
    } catch (IOException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * from the string {@code text}.
   */
  public List<Token> analyzeSyntaxText(String text) throws Exception {
    // [START language_syntax_text]
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16)
          .build();
      // analyze the syntax in the given text
      AnalyzeSyntaxResponse response = language.analyzeSyntax(request);
      // print the response
      for (Token token : response.getTokensList()) {
        System.out.printf("\tText: %s\n", token.getText().getContent());
        System.out.printf("\tBeginOffset: %d\n", token.getText().getBeginOffset());
        System.out.printf("Lemma: %s\n", token.getLemma());
        System.out.printf("PartOfSpeechTag: %s\n", token.getPartOfSpeech().getTag());
        System.out.printf("\tAspect: %s\n", token.getPartOfSpeech().getAspect());
        System.out.printf("\tCase: %s\n", token.getPartOfSpeech().getCase());
        System.out.printf("\tForm: %s\n", token.getPartOfSpeech().getForm());
        System.out.printf("\tGender: %s\n", token.getPartOfSpeech().getGender());
        System.out.printf("\tMood: %s\n", token.getPartOfSpeech().getMood());
        System.out.printf("\tNumber: %s\n", token.getPartOfSpeech().getNumber());
        System.out.printf("\tPerson: %s\n", token.getPartOfSpeech().getPerson());
        System.out.printf("\tProper: %s\n", token.getPartOfSpeech().getProper());
        System.out.printf("\tReciprocity: %s\n", token.getPartOfSpeech().getReciprocity());
        System.out.printf("\tTense: %s\n", token.getPartOfSpeech().getTense());
        System.out.printf("\tVoice: %s\n", token.getPartOfSpeech().getVoice());
        System.out.println("DependencyEdge");
        System.out.printf("\tHeadTokenIndex: %d\n", token.getDependencyEdge().getHeadTokenIndex());
        System.out.printf("\tLabel: %s\n\n", token.getDependencyEdge().getLabel());
      }
      return response.getTokensList();
    }
    // [END language_syntax_text]
  }

  /**
   * Detects categories in text using the Language Beta API.
   */
  public void classifyText(String text) throws Exception {
    // [START language_classify_text]
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set content to the text string
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
          .setDocument(doc)
          .build();
      // detect categories in the given text
      ClassifyTextResponse response = language.classifyText(request);

      for (ClassificationCategory category : response.getCategoriesList()) {
        System.out.printf("Category name : %s, Confidence : %.3f\n",
            category.getName(), category.getConfidence());
      }
    }
    // [END language_classify_text]
  }

  /**
   * Detects the entity sentiments in the string {@code text} using the Language Beta API.
   */
  public void entitySentimentText(String text) throws Exception {
    // [START language_entity_sentiment_text]
    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16).build();
      // detect entity sentiments in the given string
      AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
      // Print the response
      for (Entity entity : response.getEntitiesList()) {
        System.out.printf("Entity: %s\n", entity.getName());
        System.out.printf("Salience: %.3f\n", entity.getSalience());
        System.out.printf("Sentiment : %s\n", entity.getSentiment());
        for (EntityMention mention : entity.getMentionsList()) {
          System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
          System.out.printf("Content: %s\n", mention.getText().getContent());
          System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
          System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
          System.out.printf("Type: %s\n\n", mention.getType());
        }
      }
    }
    // [END language_entity_sentiment_text]
  }

}
