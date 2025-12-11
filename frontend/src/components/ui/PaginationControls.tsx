import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
  PaginationEllipsis,
} from "@/components/ui/pagination";

type PaginationControlsProps = {
  currentPage: number;
  totalPages: number;
  totalElements: number;
  currentElements: number;
  onPageChange: (page: number) => void;
  className?: string;
};

export default function PaginationControls({
  currentPage,
  totalPages,
  totalElements,
  currentElements,
  onPageChange,
  className = "",
}: PaginationControlsProps) {
  if (totalPages <= 1) return null;

  return (
    <div className={`mt-6 ${className}`}>
      <Pagination>
        <PaginationContent>
          <PaginationItem>
            <PaginationPrevious
              onClick={() => onPageChange(Math.max(0, currentPage - 1))}
              className={currentPage === 0 ? "pointer-events-none opacity-50" : "cursor-pointer"}
            />
          </PaginationItem>

          {/* First page */}
          {currentPage > 1 && (
            <PaginationItem>
              <PaginationLink onClick={() => onPageChange(0)} className="cursor-pointer">
                1
              </PaginationLink>
            </PaginationItem>
          )}

          {/* Left ellipsis */}
          {currentPage > 2 && (
            <PaginationItem>
              <PaginationEllipsis />
            </PaginationItem>
          )}

          {/* Previous page */}
          {currentPage > 0 && (
            <PaginationItem>
              <PaginationLink onClick={() => onPageChange(currentPage - 1)} className="cursor-pointer">
                {currentPage}
              </PaginationLink>
            </PaginationItem>
          )}

          {/* Current page */}
          <PaginationItem>
            <PaginationLink isActive className="cursor-pointer">
              {currentPage + 1}
            </PaginationLink>
          </PaginationItem>

          {/* Next page */}
          {currentPage < totalPages - 1 && (
            <PaginationItem>
              <PaginationLink onClick={() => onPageChange(currentPage + 1)} className="cursor-pointer">
                {currentPage + 2}
              </PaginationLink>
            </PaginationItem>
          )}

          {/* Right ellipsis */}
          {currentPage < totalPages - 3 && (
            <PaginationItem>
              <PaginationEllipsis />
            </PaginationItem>
          )}

          {/* Last page */}
          {currentPage < totalPages - 2 && (
            <PaginationItem>
              <PaginationLink onClick={() => onPageChange(totalPages - 1)} className="cursor-pointer">
                {totalPages}
              </PaginationLink>
            </PaginationItem>
          )}

          <PaginationItem>
            <PaginationNext
              onClick={() => onPageChange(Math.min(totalPages - 1, currentPage + 1))}
              className={currentPage === totalPages - 1 ? "pointer-events-none opacity-50" : "cursor-pointer"}
            />
          </PaginationItem>
        </PaginationContent>
      </Pagination>

      {/* Page info */}
      <div className="text-center mt-4 text-sm text-muted-foreground">
        Showing {currentElements} of {totalElements} items
      </div>
    </div>
  );
}
