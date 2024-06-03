<script lang="ts">
	import { page } from '$app/stores';
	import type { components } from '$lib/backend/apiV1/schema';
	import rq from '$lib/rq/rq.svelte';

	let post = $state<components['schemas']['PostDto'] | null>(null);
	let errorMessage = $state<string | null>(null);

	async function getPost() {
		const { data, error } = await rq.getClient().GET('/api/v1/posts/{id}', {
			params: {
				path: {
					id: parseInt($page.params.id)
				}
			}
		});

		if (data) {
			post = data.data.item;
		} else if (error) {
			errorMessage = error.msg;
		}
	}

	$effect(() => {
		getPost();
	});
</script>

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
	<a href="/post/list">리스트</a>
	<button type="button" onclick={() => history.back()}>뒤로가기</button>
</div>
