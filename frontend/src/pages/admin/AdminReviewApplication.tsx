import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import type { AdoptionApplication } from "@/types/types";
import { AdoptionApplicationService } from "@/api/adoptionApplication";
import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import { ArrowLeftIcon } from "@heroicons/react/24/outline";
import ApproveConfirmDialog from "./dialogs/ApproveConfirmDialog";
import RejectConfirmDialog from "./dialogs/RejectConfirmDialog";
import { useAuth } from "@/context/AuthProvider";

export default function AdminReviewApplication() {
  const location = useLocation();
  const navigate = useNavigate();
  const auth = useAuth();
  const application = location.state?.application as AdoptionApplication | undefined;

  const [isApproveOpen, setIsApproveOpen] = useState(false);
  const [isRejectOpen, setIsRejectOpen] = useState(false);

  if (!application) {
    return (
      <div className="space-y-6">
        <Button variant="outline" onClick={() => navigate("/admin/applications")}>
          <ArrowLeftIcon />
          Back to Applications
        </Button>
        <Card>
          <CardContent className="py-8">
            <p className="text-muted-foreground text-center">Application not found.</p>
          </CardContent>
        </Card>
      </div>
    );
  }

  const handleApprove = async () => {
    if (!auth?.user?.id) return;
    await AdoptionApplicationService.approveApplication(application.id, new Date().toISOString().split("T")[0], auth.user.id);
    navigate("/admin/applications");
  };

  const handleReject = async () => {
    if (!auth?.user?.id) return;
    await AdoptionApplicationService.rejectApplication(application.id, auth.user.id);
    navigate("/admin/applications");
  };

  const getStatusBadge = (status: string) => {
    const statusMap: Record<string, { variant: "default" | "secondary" | "destructive" | "outline" }> = {
      PENDING: { variant: "secondary" },
      APPROVED: { variant: "default" },
      REJECTED: { variant: "destructive" },
    };
    const config = statusMap[status] || { variant: "outline" };
    return <Badge variant={config.variant}>{status}</Badge>;
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div className="flex items-center gap-3 md:gap-4">
          <Button variant="outline" size="icon" className="shrink-0" onClick={() => navigate("/admin/applications")}>
            <ArrowLeftIcon className="h-4 w-4" />
          </Button>
          <div>
            <h2 className="text-xl md:text-3xl font-bold">Review Application</h2>
            <p className="text-muted-foreground text-xs md:text-sm">
              Submitted{" "}
              {new Date(application.applicationDate).toLocaleDateString("en-US", {
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            </p>
          </div>
        </div>
        {getStatusBadge(application.status)}
      </div>

      <Card>
        <CardHeader className="border-b">
          <div className="flex flex-col sm:flex-row items-start justify-between gap-4 sm:gap-6">
            {application.animal.imageUrl && (
              <div className="shrink-0">
                <img
                  src={application.animal.imageUrl}
                  alt={application.animal.name}
                  className="w-20 h-20 md:w-24 md:h-24 rounded-lg object-cover border shadow-sm"
                />
              </div>
            )}
            <div className="flex-1">
              <CardTitle className="text-lg md:text-2xl">
                {application.user.firstName} {application.user.lastName}
              </CardTitle>
              <p className="text-muted-foreground text-sm md:text-base mt-1">wants to adopt {application.animal.name}</p>
            </div>
          </div>
        </CardHeader>

        <CardContent className="space-y-8 pt-6">
          {/* Applicant Section */}
          <div>
            <h3 className="text-base md:text-lg font-semibold mb-4 flex items-center gap-2">
              <p className="">Applicant Details</p>
            </h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6 pl-4 md:pl-7">
              <div>
                <p className="text-sm text-muted-foreground mb-1">Email Address</p>
                <p className="font-medium">{application.user.email}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Phone Number</p>
                <p className="font-medium">{application.user.phone || "Not provided"}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Account Type</p>
                <Badge variant="outline">{application.user.role}</Badge>
              </div>
            </div>
          </div>

          {/* Divider */}
          <Separator />

          {/* Animal Section */}
          <div>
            <h3 className="text-base md:text-lg font-semibold mb-4 flex items-center gap-2">
              <p className="">Animal Details</p>
            </h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6 pl-4 md:pl-7">
              <div>
                <p className="text-sm text-muted-foreground mb-1">Name</p>
                <p className="font-medium text-lg">{application.animal.name}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Species & Breed</p>
                <p className="font-medium">
                  {application.animal.species.name}
                  {application.animal.breed && ` • ${application.animal.breed.name}`}
                </p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Sex</p>
                <p className="font-medium capitalize">{application.animal.sex}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Current Status</p>
                <Badge variant="secondary">{application.animal.status}</Badge>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Adoption Fee</p>
                <p className="font-medium text-lg">${application.animal.price}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Birth Date</p>
                <p className="font-medium">
                  {new Date(application.animal.birthDate).toLocaleDateString("en-US", {
                    year: "numeric",
                    month: "short",
                    day: "numeric",
                  })}
                </p>
              </div>
            </div>
          </div>

          {/* Divider */}
          <Separator />

          {/* Application Message */}
          <div>
            <h3 className="text-base md:text-lg font-semibold mb-4 flex items-center gap-2">
              <p className="text-primary">Application Description</p>
            </h3>
            <div className="pl-4 md:pl-7">
              <div className="bg-muted/50 rounded-lg p-3 md:p-4 border">
                <p className="text-xs md:text-sm leading-relaxed whitespace-pre-wrap">{application.description}</p>
              </div>
            </div>
          </div>

          {/* Review Info */}
          {application.reviewedByUser && (
            <>
              <div className="border-t" />
              <div>
                <h3 className="text-base md:text-lg font-semibold mb-4 flex items-center gap-2">
                  <span className="text-primary">✓</span> Review Information
                </h3>
                <div className="pl-4 md:pl-7">
                  <p className="text-sm text-muted-foreground mb-1">Reviewed By</p>
                  <p className="font-medium">
                    {application.reviewedByUser.firstName} {application.reviewedByUser.lastName}
                  </p>
                </div>
              </div>
            </>
          )}
        </CardContent>

        {/* Actions Footer */}
        {application.status === "PENDING" && (
          <div className="border-t bg-muted/20 px-4 md:px-6 py-4">
            <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-3 sm:gap-4">
              <p className="text-xs md:text-sm text-muted-foreground">Make a decision on this application</p>
              <div className="flex gap-2 md:gap-3">
                <Button variant="destructive" className="flex-1 sm:flex-none" onClick={() => setIsRejectOpen(true)}>
                  Reject
                </Button>
                <Button variant="default" className="flex-1 sm:flex-none bg-green-600" onClick={() => setIsApproveOpen(true)}>
                  Approve
                </Button>
              </div>
            </div>
          </div>
        )}
      </Card>

      <ApproveConfirmDialog application={application} isOpen={isApproveOpen} onClose={() => setIsApproveOpen(false)} onConfirm={handleApprove} />

      <RejectConfirmDialog application={application} isOpen={isRejectOpen} onClose={() => setIsRejectOpen(false)} onConfirm={handleReject} />
    </div>
  );
}
