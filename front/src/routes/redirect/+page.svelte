<script lang="ts">
	import { onMount } from 'svelte';
	import rq from '$lib/rq/rq.svelte';

	onMount(() => {
		const params = new URLSearchParams(window.location.search);
		const url = params.get('url');

		if (url) rq.replace(url);
	});
</script>
