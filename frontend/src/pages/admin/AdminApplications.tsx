import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { AdoptionApplicationService } from "../../api/adoptionApplication";
import type { AdoptionApplication } from "../../types/types";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ApplicationFilters from "./helpers/ApplicationFilters";
import ApplicationSortButtons from "./helpers/ApplicationSortButtons";
import { useApplicationFilters } from "@/hooks/useApplicationFilters";

export default function AdminApplications() {
  const [applications, setApplications] = useState<AdoptionApplication[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const {
    searchTerm,
    setSearchTerm,
    statusFilter,
    setStatusFilter,
    sortField,
    sortDirection,
    handleSort,
    clearFilters,
    filteredAndSortedApplications,
  } = useApplicationFilters(applications);

  useEffect(() => {
    const fetchApplications = async () => {
      try {
        setLoading(true);
        const applications = await AdoptionApplicationService.getAllApplications();
        setApplications(applications);
      } catch (error) {
        console.error("Failed to fetch applications:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchApplications();
  }, []);

  const handleReview = (application: AdoptionApplication) => {
    navigate(`/admin/applications/${application.id}`, {
      state: { application },
    });
  };


  if (loading) {
    return (
      <div className="space-y-6">
        <h2 className="text-3xl font-bold">Adoption Applications</h2>
        <Card>
          <CardContent className="py-8">
            <p className="text-muted-foreground text-center">Loading applications...</p>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-3xl font-bold">Adoption Applications</h2>
        <Badge variant="outline" className="text-base px-3 py-1">
          {applications.length} Total
        </Badge>
      </div>

      <ApplicationFilters
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
        statusFilter={statusFilter}
        setStatusFilter={setStatusFilter}
        clearFilters={clearFilters}
        totalCount={applications.length}
        filteredCount={filteredAndSortedApplications.length}
      />

      <ApplicationSortButtons sortField={sortField} sortDirection={sortDirection} onSort={handleSort} />

      <Card>
        <CardHeader>
          <CardTitle>All Applications</CardTitle>
        </CardHeader>
        <CardContent>
          {filteredAndSortedApplications.length === 0 ? (
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
                {filteredAndSortedApplications.map((application) => (
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
                        className={application.status === "APPROVED" ? "bg-green-500 hover:bg-green-600" : ""}>
                        {application.status === "PENDING"
                          ? "Pending"
                          : application.status === "APPROVED"
                          ? "Approved"
                          : "Rejected"}
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
        </CardContent>
      </Card>
    </div>
  );
}
