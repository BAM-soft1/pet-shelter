import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import PaginationControls from "@/components/ui/PaginationControls";
import SearchAndFilter from "@/components/ui/SearchAndFilter";
import { AdoptionApplicationService } from "../../api/adoptionApplication";
import type { AdoptionApplication } from "../../types/types";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function AdminApplications() {
  const [applications, setApplications] = useState<AdoptionApplication[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Pagination state
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Filter state
  const [searchInput, setSearchInput] = useState("");
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");

  const handleSearch = () => {
    setSearchTerm(searchInput);
    setCurrentPage(0);
  };

  useEffect(() => {
    const fetchApplications = async () => {
      try {
        setLoading(true);
        const pageResponse = await AdoptionApplicationService.getAllApplications(currentPage, 8, "applicationDate", "desc", statusFilter, searchTerm);
        setApplications(pageResponse.content);
        setTotalPages(pageResponse.totalPages);
        setTotalElements(pageResponse.totalElements);
      } catch (error) {
        console.error("Failed to fetch applications:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchApplications();
  }, [currentPage, statusFilter, searchTerm]);

  const clearFilters = () => {
    setSearchInput("");
    setSearchTerm("");
    setStatusFilter("all");
    setCurrentPage(0);
  };

  const handleReview = (application: AdoptionApplication) => {
    navigate(`/admin/applications/${application.id}`, {
      state: { application },
    });
  };

  if (loading) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">Loading applications...</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-3xl font-bold text-gray-800">Adoption Applications</h2>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200 space-y-4">
          <SearchAndFilter
            searchValue={searchInput}
            onSearchChange={setSearchInput}
            searchPlaceholder="Search by animal, applicant name, or email..."
            onClearFilters={clearFilters}
            showFilters={false}
            showSearchButton={true}
            onSearchSubmit={handleSearch}
            showCounts={true}
            totalCount={totalElements}
            filteredCount={applications.length}
          />

          <div>
            <label className="block text-sm font-medium mb-2">Status</label>
            <div className="flex gap-2">
              <Button variant={statusFilter === "all" ? "default" : "outline"} size="sm" onClick={() => setStatusFilter("all")}>
                All
              </Button>
              <Button variant={statusFilter === "PENDING" ? "default" : "outline"} size="sm" onClick={() => setStatusFilter("PENDING")}>
                Pending
              </Button>
              <Button variant={statusFilter === "APPROVED" ? "default" : "outline"} size="sm" onClick={() => setStatusFilter("APPROVED")}>
                Approved
              </Button>
              <Button variant={statusFilter === "REJECTED" ? "default" : "outline"} size="sm" onClick={() => setStatusFilter("REJECTED")}>
                Rejected
              </Button>
            </div>
          </div>
        </div>

        <div className="p-6">
          {applications.length === 0 ? (
            <p className="text-muted-foreground text-center py-8">No applications found.</p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>ID</TableHead>
                  <TableHead>Animal</TableHead>
                  <TableHead>Applicant</TableHead>
                  <TableHead>Email</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Applied</TableHead>
                  <TableHead className="text-right">Action</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {applications.map((application) => (
                  <TableRow key={application.id}>
                    <TableCell className="font-medium">{application.id}</TableCell>
                    <TableCell>
                      <div className="flex flex-col">
                        <span className="font-medium">{application.animal.name}</span>
                        <span className="text-xs text-muted-foreground">
                          {application.animal.species.name}
                          {application.animal.breed && ` • ${application.animal.breed.name}`}
                        </span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <span className="font-medium">
                        {application.user.firstName} {application.user.lastName}
                      </span>
                    </TableCell>
                    <TableCell>
                      <span className="text-sm">{application.user.email}</span>
                    </TableCell>
                    <TableCell>
                      <Badge
                        variant={application.status === "REJECTED" ? "destructive" : "default"}
                        className={application.status === "APPROVED" ? "bg-green-500 hover:bg-green-600" : ""}
                      >
                        {application.status === "PENDING" ? "Pending" : application.status === "APPROVED" ? "Approved" : "Rejected"}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <span className="text-sm text-muted-foreground">
                        {new Date(application.applicationDate).toLocaleDateString("en-US", {
                          year: "numeric",
                          month: "short",
                          day: "numeric",
                        })}
                      </span>
                    </TableCell>
                    <TableCell className="text-right">
                      <Button variant="outline" size="sm" onClick={() => handleReview(application)}>
                        Review
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </div>
      </div>

      <PaginationControls currentPage={currentPage} totalPages={totalPages} totalElements={totalElements} onPageChange={setCurrentPage} />
    </div>
  );
}
