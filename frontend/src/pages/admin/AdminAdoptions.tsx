import { useEffect, useState, useCallback } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Spinner } from "@/components/ui/spinner";
import PaginationControls from "@/components/ui/PaginationControls";
import { AdoptionService } from "@/api/adoptions";
import { getErrorMessage } from "@/services/fetchUtils";
import type { Adoption } from "@/types/types";

export default function AdminAdoptions() {
  const [adoptions, setAdoptions] = useState<Adoption[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize] = useState(10);

  const fetchAdoptions = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);
      const pageResponse = await AdoptionService.getAdoptions(currentPage, pageSize, "adoptionDate", "desc");
      setAdoptions(pageResponse.content);
      setTotalPages(pageResponse.totalPages);
      setTotalElements(pageResponse.totalElements);
    } catch (err) {
      console.error("Error fetching adoptions:", err);
      setError(getErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }, [currentPage, pageSize]);

  useEffect(() => {
    fetchAdoptions();
  }, [fetchAdoptions]);

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-[400px]">
        <Spinner className="size-8" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold">Completed Adoptions</h2>
        <p className="text-muted-foreground mt-2">View all successful animal adoptions</p>
      </div>

      {error && (
        <Card className="border-destructive">
          <CardContent className="pt-6">
            <p className="text-destructive font-medium">Error: {error}</p>
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader>
          <CardTitle>
            Recent Adoptions
            <span className="text-muted-foreground font-normal ml-2">({adoptions.length} total)</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          {adoptions.length === 0 ? (
            <p className="text-muted-foreground text-center py-8">No adoptions found.</p>
          ) : (
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Animal</TableHead>
                    <TableHead>Species</TableHead>
                    <TableHead>Breed</TableHead>
                    <TableHead>Adopter</TableHead>
                    <TableHead>Contact</TableHead>
                    <TableHead>Adoption Date</TableHead>
                    <TableHead className="text-right">Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {adoptions.map((adoption) => (
                    <TableRow key={adoption.id}>
                      <TableCell className="font-medium">{adoption.animal.name}</TableCell>
                      <TableCell>{adoption.animal.species.name}</TableCell>
                      <TableCell>{adoption.animal.breed?.name || "N/A"}</TableCell>
                      <TableCell>
                        {adoption.user.firstName} {adoption.user.lastName}
                      </TableCell>
                      <TableCell>
                        <div className="text-sm">
                          <div>{adoption.user.email}</div>
                          {adoption.user.phone && <div className="text-muted-foreground">{adoption.user.phone}</div>}
                        </div>
                      </TableCell>
                      <TableCell>{formatDate(adoption.adoptionDate)}</TableCell>
                      <TableCell className="text-right">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                          {adoption.animal.status}
                        </span>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>

      <PaginationControls currentPage={currentPage} totalPages={totalPages} totalElements={totalElements} onPageChange={setCurrentPage} />
    </div>
  );
}
