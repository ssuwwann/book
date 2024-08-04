<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
      .main-container {
          margin: 250px auto;
          max-width: 500px;
          width: 100%;
      }

      #passwordForm {
          background-color: darkseagreen;
          display: flex;
          flex-direction: column;
          align-items: center;
          align-self: center;
          border-radius: 10px;
          width: 500px;
          height: 400px;

      }

      #login-password {
          margin-top: 150px;
      }

      #login-btn {
          margin-top: 50px;
      }

      .hide {
          display: none;
          overflow: hidden;
      }

      .password {
          position: relative;
          margin-top: 50px;
      }

      .button-disabled {
          background-color: #ddd; /* 비활성화된 버튼 색상 */
          cursor: not-allowed; /* 커서 스타일 */
      }
  </style>
</head>
<body>
<main>
  <div class="main-container">
    <form id="passwordForm" action="/member/member.do?method=modifyForm" method="post">

      <input type="password" id="login-password" name="password" placeholder="비밀번호 확인"/>
      <br/>
      <div class="login-password-msg hide">비밀번호를 정확히 입력해 주세요.</div>

      <button id="login-btn" class="login button-disabled" type="button" disabled>확인</button>

    </form>
  </div>
</main>


<script>
  const passwordCheck = () => {
    const pwdMsg = document.querySelector('.login-password-msg');
    const loginBtn = document.getElementById('login-btn');

    $.ajax({
      url: '/member/member.do?method=passwordCheck',
      type: 'POST',
      data: {
        password: $('#login-password').val()
      },
      success: (data) => {
        if (data.result === 1) {
          // 비밀번호가 맞으면 버튼 활성화 및 클릭 시 폼 제출
          loginBtn.classList.remove('button-disabled');
          pwdMsg.classList.add('hide');
          loginBtn.disabled = false;
          loginBtn.addEventListener('click', () => {
            document.getElementById('passwordForm').submit();
          });
        } else if (data.result === -1) {
          // 비밀번호가 틀리면 오류 메시지 표시 및 버튼 비활성화
          pwdMsg.classList.remove('hide');
          loginBtn.classList.add('button-disabled');
          loginBtn.disabled = true;
        }
      },
      error: (jqXHR, textStatus, errorThrown) => {
        console.error('Error:', textStatus, errorThrown);
      },
    });
  };

  const loginBtn = document.getElementById('login-btn');
  const password = document.getElementById('login-password');

  password.addEventListener('input', passwordCheck); // 입력이 있을 때마다 비밀번호 체크

  password.addEventListener('keydown', (evt) => {
    if (evt.key === 'Enter') {
      evt.preventDefault();
      passwordCheck();
    }
  });
</script>

</body>
</html>