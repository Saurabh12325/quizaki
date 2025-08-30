package Quiz.QuizWebApplication.Config;

import Quiz.QuizWebApplication.JWTAuthorisation.JwtAuthFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
             .authorizeHttpRequests(authorize -> authorize.requestMatchers("/admin/**","/quizzes/category/**","/quizzes/**","/quizzes/AllQuestions","/player/registerPlayer","/player/verifyOtp","/player/LogOut","/player/SavePlayer","/player/leaderboard/**","/admin/fetchQuiz").permitAll()
             .anyRequest().authenticated())
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
             return http.build();
    }
   @Bean
   public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }





}
