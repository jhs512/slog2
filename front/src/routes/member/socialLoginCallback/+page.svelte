<script lang="ts">
	import { page } from '$app/stores';
	import rq from '$lib/rq/rq.svelte';

	$effect(() => {
		const provierTypeCode = $page.url.searchParams.get('provierTypeCode');
		console.log(`logined by ${provierTypeCode}`);

		rq.msgInfo('환영합니다.');
		rq.replace(`/`);
	});
</script>
