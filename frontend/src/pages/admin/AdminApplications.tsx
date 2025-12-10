import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { AdoptionApplicationService } from "../../api/adoptionApplication";
import type { AdoptionApplication } from "../../types/types";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function AdminApplications() {
  const [applications, setApplications] = useState<AdoptionApplication[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchApplications = async () => {
      try {
        setLoading(true);
        const applications =
          await AdoptionApplicationService.getAllApplications();
        setApplications(applications);
        console.log("all applications", applications);
      } catch (error) {
        console.error("Failed to fetch applications:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchApplications();
  }, []);

  const handleReview = (applicationId: number) => {
    navigate(`/admin/applications/${applicationId}`);
  };

  const getStatusBadge = (status: string) => {
    const statusMap: Record<
      string,
      {
        variant: "default" | "secondary" | "destructive" | "outline";
        label: string;
      }
    > = {
      PENDING: { variant: "secondary", label: "Pending" },
      APPROVED: { variant: "default", label: "Approved" },
      REJECTED: { variant: "destructive", label: "Rejected" },
    };

    const config = statusMap[status] || { variant: "outline", label: status };
    return <Badge variant={config.variant}>{config.label}</Badge>;
  };

  if (loading) {
    return (
      <div className="space-y-6">
        <h2 className="text-3xl font-bold">Adoption Applications</h2>
        <Card>
          <CardContent className="py-8">
            <p className="text-muted-foreground text-center">
              Loading applications...
            </p>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-3xl font-bold text-gray-800">
            Application Management
          </h2>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-xl font-bold text-gray-800">
            All Applications ({applications.length})
          </h3>
        </div>
        <div className="p-6">
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
                      <span className="font-medium">
                        {application.animal.name}
                      </span>
                      <span className="text-xs text-muted-foreground">
                        {application.animal.species.name}
                        {application.animal.breed &&
                          ` • ${application.animal.breed.name}`}
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
                  <TableCell>{getStatusBadge(application.status)}</TableCell>
                  <TableCell>
                    <span className="text-sm text-muted-foreground">
                      {new Date(application.applicationDate).toLocaleDateString(
                        "en-US",
                        {
                          year: "numeric",
                          month: "short",
                          day: "numeric",
                        }
                      )}
                    </span>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleReview(application.id)}
                    >
                      Review
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </div>
    </div>
  );
}

//   return (
//     <div className="space-y-6">
//       <div className="flex items-center justify-between">
//         <h2 className="text-3xl font-bold">Adoption Applications</h2>
//         <Badge variant="outline" className="text-base px-3 py-1">
//           {applications.length} Total
//         </Badge>
//       </div>

//       <Card>
//         <CardHeader>
//           <CardTitle>All Applications</CardTitle>
//         </CardHeader>
//         <CardContent>
//           {applications.length === 0 ? (
//             <p className="text-muted-foreground text-center py-8">
//               No applications found.
//             </p>
//           ) : (
// <Table>
//   <TableHeader>
//     <TableRow>
//       <TableHead>ID</TableHead>
//       <TableHead>Animal</TableHead>
//       <TableHead>Applicant</TableHead>
//       <TableHead>Email</TableHead>
//       <TableHead>Status</TableHead>
//       <TableHead>Applied</TableHead>
//       <TableHead className="text-right">Action</TableHead>
//     </TableRow>
//   </TableHeader>
//   <TableBody>
//     {applications.map((application) => (
//       <TableRow key={application.id}>
//         <TableCell className="font-medium">
//         {application}
//         </TableCell>
//         <TableCell>
//           <div className="flex flex-col">
//             <span className="font-medium">
//               {application.animal.name}
//             </span>
//             <span className="text-xs text-muted-foreground">
//               {application.animal.species.name}
//               {application.animal.breed &&
//                 ` • ${application.animal.breed.name}`}
//             </span>
//           </div>
//         </TableCell>
//         <TableCell>
//           <span className="font-medium">
//             {application.user.firstName} {application.user.lastName}
//           </span>
//         </TableCell>
//         <TableCell>
//           <span className="text-sm">{application.user.email}</span>
//         </TableCell>
//         <TableCell>{getStatusBadge(application.status)}</TableCell>
//         <TableCell>
//           <span className="text-sm text-muted-foreground">
//             {new Date(
//               application.applicationDate
//             ).toLocaleDateString("en-US", {
//               year: "numeric",
//               month: "short",
//               day: "numeric",
//             })}
//           </span>
//         </TableCell>
//         <TableCell className="text-right">
//           <Button
//             variant="outline"
//             size="sm"
//             onClick={() => handleReview(application.id)}
//           >
//             Review
//           </Button>
//         </TableCell>
//       </TableRow>
//     ))}
//   </TableBody>
// </Table>
//           )}
//         </CardContent>
//       </Card>
//     </div>
//   );
// }
