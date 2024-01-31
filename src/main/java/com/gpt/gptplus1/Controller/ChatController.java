package com.gpt.gptplus1.Controller;
import com.gpt.gptplus1.Entity.ChatMsg;
import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Repository.ChatMsgRepo;
import com.gpt.gptplus1.Service.ChatService;
import com.gpt.gptplus1.Service.WebSecurity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
    @RequestMapping("/")
    public class ChatController {
        @Autowired
        private UserService userService;
     @Autowired
     private ChatService chatService;

        @GetMapping("/api2/profile")
        @PreAuthorize("hasRole('user')")
        public User sendUserProfile() {
            return trimUser(getUser());
        }
        @PostMapping ("/api2/saveApi")
        @PreAuthorize("hasRole('user')")
        public User saveApi(@RequestBody User user) {
            System.out.println("api = "+user.getApiKey());
            String apiKey = user.getApiKey();
            User user2 = getUser();
            user2.setApiKey(apiKey);
            userService.saveUser(user2);
         return trimUser(getUser());
        }
        public User getUser(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();
            return userService.getUserByEmail(user);
        }

        public User trimUser(User userTemp){
            userTemp.setPassword(null);
            userTemp.setVerificationCode(null);
            userTemp.setApiKey(userTemp.getApiKey() != null && userTemp.getApiKey().length() >= 6 ? userTemp.getApiKey().substring(0, 6) : userTemp.getApiKey());
            return userTemp;
        }
    @PostMapping ("/api2/textChat")
    @PreAuthorize("hasRole('user')")
    public ChatMsg textChat(@RequestBody ChatMsg chatMsg) {
        User user = getUser();
        System.out.println("role = "+chatMsg.getRole());
        System.out.println("content = "+chatMsg.getContent());
        String code = " have a full library loader at the end. For loops in Power Query. using For(x=0; x<5; x++) loops in Power Query. Simple Favorite. I use List ```<pre><code [highlight]=code (highlighted)=onHighlight($event)  ></code></pre>  imports: [RouterModule.forRoot(routes)," +
                "    BrowserModule," +
                "    FormsModule,HttpClientModule, AppRoutingModule, BrowserAnimationsModule,MatProgressSpinnerModule,AlertModule.forRoot(),HighlightModule,HighlightPlusModule," +
                "  ]," +
                "  providers: [{" +
                "    provide: HTTP_INTERCEPTORS," +
                "    useClass: AuthInterceptor," +
                "    multi: true," +
                "  }," +
                "    {" +
                "      provide: HIGHLIGHT_OPTIONS," +
                "      useValue: <HighlightOptions>{" +
                "        lineNumbers: true," +
                "        autoHighlight: true," +
                "        //coreLibraryLoader: () => import('highlight.js/lib/core')," +
                "        lineNumbersLoader: () => import('ngx-highlightjs/line-numbers')," +
                "        themePath: 'node_modules/highlight.js/styles/github.css'," +
                "        fullLibraryLoader: () => import('highlight.js')," +
                "      }," +
                "    },{" +
                "    provide: HTTP_INTERCEPTORS," +
                "    useClass: LoaderInterceptor," +
                "    multi: true" +
                "  }]," +
                "  bootstrap: [AppComponent]" +
                "})" +
                "export class AppModule { } ``` but you do it";
        //chatService.ChatService2(user,chatMsg); //return this
       // chatMsg.setContent(code);
        return chatService.ChatService2(user,chatMsg);
    }
}
