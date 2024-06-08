<script lang="ts">
	import rq from '$lib/rq/rq.svelte';

	async function submitLoginForm(this: HTMLFormElement) {
		const form: HTMLFormElement = this;

		form.username.value = form.username.value.trim();

		if (form.username.value.length === 0) {
			rq.msgError('아이디를 입력해주세요.');
			form.username.focus();

			return;
		}

		form.password.value = form.password.value.trim();

		if (form.password.value.length === 0) {
			rq.msgError('비밀번호를 입력해주세요.');
			form.password.focus();

			return;
		}

		const { data, error } = await rq.getClient().POST('/api/v1/members/login', {
			body: {
				username: form.username.value,
				password: form.password.value
			}
		});

		if (error) rq.msgError(error.msg);
		else {
			rq.msgAndRedirect(data, undefined, '/', () => rq.setLogined(data.data.item));
		}
	}
</script>

<form on:submit|preventDefault={submitLoginForm}>
	<div>
		<div>아이디</div>
		<input type="text" name="username" placeholder="아이디" />
	</div>

	<div>
		<div>비밀번호</div>
		<input type="password" name="password" placeholder="비밀번호" autoComplete="off" />
	</div>

	<div>
		<div>로그인</div>
		<button type="submit">로그인</button>
		<a href={rq.getKakaoLoginUrl()} class="btn">
			<i class="fa-solid fa-comment text-[color:#ffe812]"></i>
			카카오로 3초만에 시작하기
		</a>
	</div>
</form>
