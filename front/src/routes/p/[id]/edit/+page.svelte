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

	async function submitEditForm(this: HTMLFormElement, event: Event) {
		event.preventDefault();

		const form: HTMLFormElement = this;

		const formTitleInput = form.elements.namedItem('title') as HTMLInputElement;
		const formBodyInput = form.body;

		const { data, error } = await rq.getClient().PUT(`/api/v1/posts/{id}`, {
			params: {
				path: {
					id: parseInt($page.params.id)
				}
			},
			body: {
				title: formTitleInput.value.trim(),
				body: formBodyInput.value.trim(),
				published: form.published.checked,
				listed: form.listed.checked
			}
		});

		if (data) {
			rq.msgInfo(data.msg);
		} else if (error) {
			rq.msgError(error.msg);
		}
	}

	$effect(() => {
		getPost();
	});
</script>

<h1>{$page.params.id}번 글 수정</h1>

{#if post}
	<form onsubmit={submitEditForm}>
		<div>
			<div>공개</div>
			<input type="checkbox" name="published" value={true} checked={post.published} />
		</div>

		<div>
			<div>검색허용</div>
			<input type="checkbox" name="listed" value={true} checked={post.listed} />
		</div>

		<div>
			<div>제목</div>
			<input type="text" name="title" value={post.title} placeholder="제목" />
		</div>

		<div>
			<div>내용</div>
			<textarea name="body" placeholder="내용">{post.body}</textarea>
		</div>

		<div>
			<div>저장</div>
			<button type="submit">저장</button>
		</div>
	</form>
{:else if errorMessage}
	<div>{errorMessage}</div>
{/if}

<div>
	<a href="/p/list">리스트</a>
	<button type="button" onclick={() => history.back()}>뒤로가기</button>
</div>
