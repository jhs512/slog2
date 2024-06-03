import type { PageLoad } from './$types';
import rq from '$lib/rq/rq.svelte';

export const load: PageLoad = async ({ params, fetch }) => {
	const { data, error } = await rq.getClientWithFetch(fetch).GET(`/api/v1/posts/{id}`, {
		params: {
			path: {
				id: parseInt(params.id)
			}
		}
	});

	return {
		data,
		error
	};
};
