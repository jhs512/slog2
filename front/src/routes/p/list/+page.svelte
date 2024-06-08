<script lang="ts">
	import type { components } from '$lib/backend/apiV1/schema';
	import rq from '$lib/rq/rq.svelte';

	let posts = $state<components['schemas']['PostDto'][]>([]);

	async function getPosts() {
		const { data, error } = await rq.getClient().GET('/api/v1/posts');

		if (data) {
			posts = data.data.items;
		}
	}

	$effect(() => {
		getPosts();
	});
</script>

<h1>SURL 목록</h1>

<ul>
	{#each posts as post (post.id)}
		<li>
			<a href="/p/{post.id}">{post.id}</a> : {post.title}
			<br />
			{#if post.actorCanEdit}
				<a href="/p/{post.id}/edit">수정</a>
			{/if}
		</li>
	{/each}
</ul>
