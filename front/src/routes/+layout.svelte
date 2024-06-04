<script lang="ts">
	import rq from '$lib/rq/rq.svelte';

	const { children } = $props();

	$effect(() => {
		rq.initAuth();
	});
</script>

<ul>
	<li><a href="/p/list"> 글</a></li>
	{#if rq.isLogout()}
		<li>
			<a href="/member/login"> 로그인</a>
		</li>
	{/if}
	{#if rq.isLogin()}
		<li>
			<button onclick={() => rq.logoutAndRedirect('/')}> 로그아웃</button>
		</li>
		<li>
			<a href="/member/me">
				<img width="50" src={rq.member.profileImgUrl} alt="" />
				{rq.member.nickname}님의 정보
			</a>
		</li>
	{/if}
</ul>

<main>{@render children()}</main>
