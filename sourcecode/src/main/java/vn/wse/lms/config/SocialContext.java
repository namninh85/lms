package vn.wse.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource(value={"classpath:/spring/applicationContext-social.xml"})
public class SocialContext  {

}
