package com.github.stephenwanjala.demospringfx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HelloController {
    @FXML
    private TextField todoTitle;

    @FXML
    private ListView<String> todoListView;

    private final ObservableList<String> todoItems = FXCollections.observableArrayList();

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String baseUrl = "http://localhost:8080/api/todos";

    @FXML
    public void initialize() {
        todoListView.setItems(todoItems);
        loadTodos();
    }

    @FXML
    public void addTodo() {
        String title = todoTitle.getText();
        if (title.isEmpty()) return;

        String json = gson.toJson(new TodoDto(title, false));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    loadTodos();
                }
            }
        });
    }

    private void loadTodos() {
        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<TodoDto>>() {}.getType();
                    List<TodoDto> todos = gson.fromJson(responseBody, listType);

                    javafx.application.Platform.runLater(() -> {
                        todoItems.clear();
                        for (TodoDto todo : todos) {
                            todoItems.add(todo.title());
                        }
                    });
                }
            }
        });
    }

    public record TodoDto( String title, boolean completed) {
    }
}