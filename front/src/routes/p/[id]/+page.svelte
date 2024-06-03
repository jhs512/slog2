<script lang="ts">
	import { page } from '$app/stores';

	const { data: pageData } = $props();

	const post = pageData.data?.data.item;
	const errorMessage = pageData.error?.msg;
</script>

<svelte:head>
	{#if post}
		<title>{post.title}</title>
		<meta
			name="description"
			content={post.body.length > 10 ? post.body.substring(0, 10) + '...' : post.body}
		/>
	{:else}
		<title>404</title>
		<meta name="description" content="404" />
	{/if}
</svelte:head>

<h1>{$page.params.id}번 SURL 페이지</h1>

{#if post}
	<div>
		ID : {post.id}
	</div>

	<div>
		생성날짜 : {post.createDate}
	</div>

	<div>
		수정날짜 : {post.modifyDate}
	</div>

	<div>
		제목 : {post.title}
	</div>

	<div>
		내용 : {post.body}
	</div>
{:else if errorMessage}
	<div>{errorMessage}</div>
{/if}

<div>
	<a href="/p/list">리스트</a>
	<button type="button" onclick={() => history.back()}>뒤로가기</button>
</div>
