package Controller;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
     /**
     * Method defines the structure of the Javalin Social Media API. Javalin methods will use handler methods
     * to manipulate the Context object, which is a special object provided by Javalin which contains information about
     * HTTP requests and can generate responses. 
     **/
    public Javalin startAPI() {

        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postAccountLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessagesByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesHandlerByUser);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        return app;
    }
     /**
     * Handler to post a user login.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     **/
    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginaccount = accountService.loginAccount(account);
        if (loginaccount != null) {
            ctx.json(mapper.writeValueAsString(loginaccount));
        } else {
            ctx.status(401);
        }
    }
    /**
     * Handler to post a user register.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     **/
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if (account.getUsername().length() < 1 || account.getPassword().length() < 4) {
            ctx.status(400);
        } 
        else {
            Account addedAccount = accountService.addAccount(account);
            if (addedAccount != null) {
                ctx.json(mapper.writeValueAsString(addedAccount));
            } else {
                ctx.status(400);
            }

        }
    }
    /**
     * Handler to get all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
    **/
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    /**
     * Handler to get message by message_id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     **/
    private void getMessagesByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messages = messageService.getMessagesById(message_id);
        if (messages == null) {
            ctx.status(200);
        } else {
            ctx.json(messages);
        }
    }
    /**
     * Handler to delete a message by message_id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     **/
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messages = messageService.deleteMessagesById(message_id);
        if (messages == null) {
            ctx.status(200);
        } else {
            ctx.json(messages);
        }
    }
    /**
     * Handler to patch a message by message_id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     **/
    private void patchMessagesByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = mapper.readTree(ctx.body()).get("message_text").asText();

        if (message_text.length() < 1 || message_text.length() > 255) {
            ctx.status(400);
        } 
        else {
            Message message = messageService.updateMessageById(message_id, message_text);
            if (message == null) {
                ctx.status(400);
            } 
            else {
                ctx.json(message);
            }
        }
    }
    /**
     * Handler to post a new message.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     **/
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if (message.getMessage_text().length() < 1 || message.getMessage_text().length() > 255) {
            ctx.status(400);
        } 
        else {
            Message addedMessage = messageService.addMessage(message);
            if (addedMessage != null) {
                ctx.json(mapper.writeValueAsString(addedMessage));
            } 
            else {
                ctx.status(400);
            }
        }

    }

    /**
     * Handler to get all messages by a user.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     **/
    public void getAllMessagesHandlerByUser(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(account_id);
        if (messages == null) {
            ctx.status(200);
        } else 
        {
            ctx.json(messages);
        }
    }

}