package org.zerock.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
  "file:src/main/webapp/WEB-INF/spring/root-context.xml",
  "file:src/main/webapp/WEB-INF/spring/security-context.xml"
  })
@Log4j
public class PasswordEncodingServiceTest {

  @Setter(onMethod_ = @Autowired)
  private PasswordEncoder pwencoder;
  
  @Setter(onMethod_ = @Autowired)
  private DataSource ds;
  
  @Test
  public void testUpdateMemberPassword() {

    String selectSql = "SELECT userid, userpw FROM LuCK_member";
    String updateSql = "UPDATE LuCK_member SET userpw = ? WHERE userid = ?";
    
    Connection con = null;
    PreparedStatement selectPstmt = null;
    PreparedStatement updatePstmt = null;
    ResultSet rs = null;
    
    try {
      con = ds.getConnection();
      selectPstmt = con.prepareStatement(selectSql);
      updatePstmt = con.prepareStatement(updateSql);
      
      rs = selectPstmt.executeQuery();
      
      while (rs.next()) {
        String userid = rs.getString("userid");
        String userpw = rs.getString("userpw");
        
        // 비밀번호가 20자 이하인 경우만 처리
        if (userpw != null && userpw.length() <= 20) {
          String encodedPw = pwencoder.encode(userpw);
        
          updatePstmt.setString(1, encodedPw);
          updatePstmt.setString(2, userid);
        
          updatePstmt.executeUpdate();
          
          log.info("Updated password for userid: " + userid);
        } else {
          log.info("Skipped userid: " + userid + " due to password length greater than 20 characters.");
        }
      }
      
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if (rs != null) { try { rs.close(); } catch(Exception e) {} }
      if (selectPstmt != null) { try { selectPstmt.close(); } catch(Exception e) {} }
      if (updatePstmt != null) { try { updatePstmt.close(); } catch(Exception e) {} }
      if (con != null) { try { con.close(); } catch(Exception e) {} }
    }
  }
  
  // 다른 테스트 메서드들...
}

