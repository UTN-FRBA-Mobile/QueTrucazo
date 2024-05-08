export type Pagination = {
  page: number;
  pageSize: number;
}

export const getOffset = (pagination: Pagination) => (pagination.page - 1) * pagination.pageSize;

export type PaginationResult = Pagination & {
  total: number;
}

export type EntriesResult<T> = {
  entries: T[];
  pagination: PaginationResult;
};