package org.zerock.security;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DeleteAllMemberData {

  @Setter(onMethod_ = @Autowired)
  private DataSource ds;
  
  @Test
  public void testDeleteAllMembersAndAuth() {

    String deleteAuthSql = "DELETE FROM LuCK_member_auth";
    String deleteMemberSql = "DELETE FROM LuCK_member";
    
    Connection con = null;
    PreparedStatement pstmt = null;
    
    try {
      con = ds.getConnection();
      
      // LuCK_member_auth 테이블 데이터 삭제
      pstmt = con.prepareStatement(deleteAuthSql);
      int authDeletedRows = pstmt.executeUpdate();
      log.info("Deleted rows from LuCK_member_auth: " + authDeletedRows);
      
      pstmt.close();  // 이전 PreparedStatement 닫기
      
      // LuCK_member 테이블 데이터 삭제
      pstmt = con.prepareStatement(deleteMemberSql);
      int memberDeletedRows = pstmt.executeUpdate();
      log.info("Deleted rows from LuCK_member: " + memberDeletedRows);
      
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if (pstmt != null) { try { pstmt.close(); } catch(Exception e) {} }
      if (con != null) { try { con.close(); } catch(Exception e) {} }
    }
  }
  
  // 다른 테스트 메서드들...
}
