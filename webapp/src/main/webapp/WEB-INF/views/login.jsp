<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head></head>
<body>
   <h1>Login</h1>
   <form name='f' action="login" method='POST'>
   <input type="hidden" name=${ _csrf.parameterName } value="${ _csrf.token }" />
      <table>
         <tr>
            <td><fmt:message key="input.user" />:</td>
            <td><input type='email' name='username' value='' required /></td>
         </tr>
         <tr>
            <td><fmt:message key="input.password" />:</td>
            <td><input type='password' name='password' required /></td>
         </tr>
         <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
         </tr>
      </table>
      
    
  </form>
</body>
</html>