import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import type { AdoptionApplication, AuthUser } from "@/types/types";
import { AdoptionApplicationService } from "@/api/adoptionApplication";
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { ArrowLeftIcon } from "@heroicons/react/24/outline";
import ApproveConfirmDialog from "./dialogs/ApproveConfirmDialog";
import RejectConfirmDialog from "./dialogs/RejectConfirmDialog";
import { authProvider } from "@/security/authUtils";

export default function AdminReviewApplication() {
  const location = useLocation();
  const navigate = useNavigate();
  const application = location.state?.application as
    | AdoptionApplication
    | undefined;
  const [user, setUser] = useState<AuthUser>(null);

  const [isApproveOpen, setIsApproveOpen] = useState(false);
  const [isRejectOpen, setIsRejectOpen] = useState(false);

  if (!application) {
    return (
      <div className="space-y-6">
        <Button
          variant="outline"
          onClick={() => navigate("/admin/applications")}
        >
          <ArrowLeftIcon />
          Back to Applications
        </Button>
        <Card>
          <CardContent className="py-8">
            <p className="text-muted-foreground text-center">
              Application not found.
            </p>
          </CardContent>
        </Card>
      </div>
    );
  }

  useEffect(() => {
    const fetchUserData = async () => {
      const token = localStorage.getItem("token") || "";
      const userData = await authProvider.getCurrentUser(token);
      setUser(userData);
    };
    fetchUserData();
  }, [isApproveOpen, isRejectOpen]);

  const handleApprove = async () => {
    await AdoptionApplicationService.approveApplication(
      application.id,
      new Date().toISOString().split('T')[0],
      user.id
    );
    navigate("/admin/applications");
  };

  const handleReject = async () => {
    await AdoptionApplicationService.rejectApplication(application.id, user.id);
    navigate("/admin/applications");
  };

  const getStatusBadge = (status: string) => {
    const statusMap: Record<
      string,
      { variant: "default" | "secondary" | "destructive" | "outline" }
    > = {
      PENDING: { variant: "secondary" },
      APPROVED: { variant: "default" },
      REJECTED: { variant: "destructive" },
    };
    const config = statusMap[status] || { variant: "outline" };
    return <Badge variant={config.variant}>{status}</Badge>;
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button
            variant="outline"
            size="icon"
            onClick={() => navigate("/admin/applications")}
          >
            <ArrowLeftIcon />
          </Button>
          <div>
            <h2 className="text-3xl font-bold">Review Application</h2>
            <p className="text-muted-foreground text-sm">
              Submitted{" "}
              {new Date(application.applicationDate).toLocaleDateString(
                "en-US",
                {
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                }
              )}
            </p>
          </div>
        </div>
        {getStatusBadge(application.status)}
      </div>

      {/* Main Application Card */}
      <Card>
        <CardHeader className="border-b">
          <div className="flex items-start justify-between gap-6">
            {application.animal.imageUrl && (
              <div className="shrink-0">
                <img
                  src={application.animal.imageUrl}
                  alt={application.animal.name}
                  className="w-24 h-24 rounded-lg object-cover border shadow-sm"
                />
              </div>
            )}
            <div className="flex-1">
              <CardTitle className="text-2xl">
                {application.user.firstName} {application.user.lastName}
              </CardTitle>
              <p className="text-muted-foreground mt-1">
                wants to adopt {application.animal.name}
              </p>
            </div>
          </div>
        </CardHeader>

        <CardContent className="space-y-8 pt-6">
          {/* Applicant Section */}
          <div>
            <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
              <span className="text-primary">👤</span> Applicant Details
            </h3>
            <div className="grid grid-cols-3 gap-6 pl-7">
              <div>
                <p className="text-sm text-muted-foreground mb-1">
                  Email Address
                </p>
                <p className="font-medium">{application.user.email}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">
                  Phone Number
                </p>
                <p className="font-medium">
                  {application.user.phone || "Not provided"}
                </p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">
                  Account Type
                </p>
                <Badge variant="outline">{application.user.role}</Badge>
              </div>
            </div>
          </div>

          {/* Divider */}
          <Separator />

          {/* Animal Section */}
          <div>
            <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
              <span className="text-primary">🐾</span> Animal Details
            </h3>
            <div className="grid grid-cols-3 gap-6 pl-7">
              <div>
                <p className="text-sm text-muted-foreground mb-1">Name</p>
                <p className="font-medium text-lg">{application.animal.name}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">
                  Species & Breed
                </p>
                <p className="font-medium">
                  {application.animal.species.name}
                  {application.animal.breed &&
                    ` • ${application.animal.breed.name}`}
                </p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Sex</p>
                <p className="font-medium capitalize">
                  {application.animal.sex}
                </p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">
                  Current Status
                </p>
                <Badge variant="secondary">{application.animal.status}</Badge>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">
                  Adoption Fee
                </p>
                <p className="font-medium text-lg">
                  ${application.animal.price}
                </p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground mb-1">Birth Date</p>
                <p className="font-medium">
                  {new Date(application.animal.birthDate).toLocaleDateString(
                    "en-US",
                    {
                      year: "numeric",
                      month: "short",
                      day: "numeric",
                    }
                  )}
                </p>
              </div>
            </div>
          </div>

          {/* Divider */}
          <Separator />

          {/* Application Message */}
          <div>
            <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
              <span className="text-primary">💬</span> Application Message
            </h3>
            <div className="pl-7">
              <div className="bg-muted/50 rounded-lg p-4 border">
                <p className="text-sm leading-relaxed whitespace-pre-wrap">
                  {application.description}
                </p>
              </div>
            </div>
          </div>

          {/* Review Info */}
          {application.reviewedByUser && (
            <>
              <div className="border-t" />
              <div>
                <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  <span className="text-primary">✓</span> Review Information
                </h3>
                <div className="pl-7">
                  <p className="text-sm text-muted-foreground mb-1">
                    Reviewed By
                  </p>
                  <p className="font-medium">
                    {application.reviewedByUser.firstName}{" "}
                    {application.reviewedByUser.lastName}
                  </p>
                </div>
              </div>
            </>
          )}
        </CardContent>

        {/* Actions Footer */}
        {application.status === "PENDING" && (
          <div className="border-t bg-muted/20 px-6 py-4">
            <div className="flex items-center justify-between">
              <p className="text-sm text-muted-foreground">
                Make a decision on this application
              </p>
              <div className="flex gap-3">
                <Button
                  variant="destructive"
                  onClick={() => setIsRejectOpen(true)}
                >
                  Reject
                </Button>
                <Button
                  variant="default"
                  className="bg-green-600"
                  onClick={() => setIsApproveOpen(true)}
                >
                  Approve Application
                </Button>
              </div>
            </div>
          </div>
        )}
      </Card>

      <ApproveConfirmDialog
        application={application}
        isOpen={isApproveOpen}
        onClose={() => setIsApproveOpen(false)}
        onConfirm={handleApprove}
      />

      <RejectConfirmDialog
        application={application}
        isOpen={isRejectOpen}
        onClose={() => setIsRejectOpen(false)}
        onConfirm={handleReject}
      />
    </div>
  );
}
