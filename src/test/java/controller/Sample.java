package controller;

import com.aston.infoBoardRestService.exception.UserNotFoundException;
import com.aston.infoBoardRestService.service.UserService;
import com.aston.infoBoardRestService.servlet.api.UserController;
//import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Sample {
    private final String SERVLET_PATH = "/authors";
    private final String REQUEST_URI_WITH_PATH_VARIABLE = "servlet_rest_war/authors/1";
    private final String REQUEST_URI_WITHOUT_PATH_VARIABLE = "servlet_rest_war/authors";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private UserController servlet;
    @Mock
    private UserService service;
    private StringWriter jsonResponse;

//    @Test
//    void testGetRequest() throws IOException, JSONException {
//        mockRequestPath(true);
//        mockResponseWriter();
//        String expectedJson = """
//                {
//                    "id": 1,
//                    "first_name": "im first",
//                    "second_name": "im second",
//                    "third_name": "im third",
//                    "books_name": [
//                        "book_1",
//                        "book_2"
//                   ]
//                }
//                """;
//        AuthorResponse authorResponse = new AuthorResponse(1,
//                "im first",
//                "im second",
//                "im third",
//                List.of("book_1", "book_2"));
//        when(service.getAuthorById(1)).thenReturn(authorResponse);
//        servlet.doGet(request, response);
//
//        Mockito.verify(response).setContentType("application/json");
//        Mockito.verify(response).setCharacterEncoding("UTF-8");
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//        JSONAssert.assertEquals(expectedJson, jsonResponse.toString(), JSONCompareMode.LENIENT);
//    }


    // error - getting users from real db, fix mock
//    @Test
//    void testNotExistAuthorGetRequest() throws Exception {
//        mockRequestPath(true);
//
//        given(service.getUserById(1000L)).willThrow((new UserNotFoundException()));
//
//        assertThrows(UserNotFoundException.class, () -> servlet.doGet(request, response));
//    }

//    @Test
//    void testBlankPathVariableAuthorGetRequest() throws Exception {
//        mockRequestPath(false);
//        assertThrows(PathVariableException.class, () -> servlet.doGet(request, response));
//    }

//    @Test
//    void testPutRequest() throws Exception {
//        mockRequestPath(true);
//        jsonResponse = new StringWriter();
//        final String inputJson = """
//                {
//                    "id": 1,
//                    "first_name": "im first",
//                    "second_name": "im second",
//                    "third_name": "im third"
//                }
//                """;
//        mockRequestReader(inputJson);
//        doNothing().when(service).updateAuthor(any(AuthorModifyRequest.class), eq(1));
//        servlet.doPut(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//    }
//
//    @Test
//    void testNotExistAuthorPutRequest() throws Exception {
//        mockRequestPath(true);
//        final String inputJson = """
//                {
//                    "id": 1,
//                    "first_name": "im first",
//                    "second_name": "im second",
//                    "third_name": "im third"
//                }
//                """;
//        mockRequestReader(inputJson);
//        doThrow(new AuthorNotFoundException(1)).when(service).updateAuthor(any(AuthorModifyRequest.class), eq(1));
//
//        assertThrows(AuthorNotFoundException.class, () -> servlet.doPut(request, response));
//    }
//
//    @Test
//    void testBlankPathVariableAuthorPutRequest() {
//        mockRequestPath(false);
//        assertThrows(PathVariableException.class, () -> servlet.doPut(request, response));
//    }

//    @Test
//    void testPostRequest() throws Exception {
//        jsonResponse = new StringWriter();
//        final String inputJson = """
//                {
//                    "first_name": "im first",
//                    "second_name": "im second",
//                    "third_name": "im third"
//                }
//                """;
//        mockRequestReader(inputJson);
//        doNothing().when(service).addAuthor(any(AuthorInsertRequest.class));
//        servlet.doPost(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
//    }
//
//    @Test
//    void testDeleteRequest() throws Exception {
//        mockRequestPath(true);
//        doNothing().when(service).deleteAuthorById(1);
//
//        servlet.doDelete(request, response);
//
//        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
//    }
//
//    @Test
//    void testDeleteNotExistAuthorRequest() throws Exception {
//        mockRequestPath(true);
//        doThrow(new AuthorNotFoundException(1)).when(service).deleteAuthorById(1);
//
//        assertThrows(AuthorNotFoundException.class, () -> servlet.doDelete(request, response));
//    }
//
//    @Test
//    void testBlankPathVariableAuthorDeleteRequest() {
//        mockRequestPath(false);
//        assertThrows(PathVariableException.class, () -> servlet.doDelete(request, response));
//    }

    ////////
    private void mockRequestReader(final String requestJsonStr) throws IOException {
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(requestJsonStr.getBytes(StandardCharsets.UTF_8))));
        when(request.getReader()).thenReturn(reader);
    }


    private void mockResponseWriter() throws IOException {
        jsonResponse = new StringWriter();
        PrintWriter pw = new PrintWriter(jsonResponse);
        when(response.getWriter()).thenReturn(pw);
    }

    private void mockRequestPath(boolean isPathVariableExist) {
        when(request.getServletPath()).thenReturn(SERVLET_PATH);
        if (isPathVariableExist) when(request.getRequestURI()).thenReturn(REQUEST_URI_WITH_PATH_VARIABLE);
        else when(request.getRequestURI()).thenReturn(REQUEST_URI_WITHOUT_PATH_VARIABLE);
    }
}