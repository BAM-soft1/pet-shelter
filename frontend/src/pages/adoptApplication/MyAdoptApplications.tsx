import { useState, useEffect } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import type { AdoptionApplicationResponse, AuthUser } from "../../types/types";
import { authProvider } from "@/security/authUtils";
import { AdoptionApplicationService } from "../../api/adoptionApplication";
import { getErrorMessage } from "../../services/fetchUtils";

export default function MyAdoptApplication() {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [applications, setApplications] = useState<
    AdoptionApplicationResponse[]
  >([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const token = localStorage.getItem("token") || "";
        const user = await authProvider.getCurrentUser(token);
        setUser(user ?? null);
      } catch (err) {
        setUser(null);
      }
    };

    fetchUserData();
  }, []);

  useEffect(() => {
    const fetchApplications = async () => {
      if (!user?.id) return;

      try {
        setLoading(true);
        setError(null);
        const data =
          await AdoptionApplicationService.getAdoptionApplicationForUser(
            user.id
          );
        setApplications(data);
      } catch (err) {
        setError(getErrorMessage(err));
      } finally {
        setLoading(false);
      }
    };

    fetchApplications();
  }, [user]);

  if (loading) {
    return (
      <MainLayout>
        <div className="text-center py-12">
          <p className="text-muted-foreground">Loading applications...</p>
        </div>
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout>
        <div className="text-center py-12">
          <p className="text-destructive">
            Error loading applications: {error}
          </p>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-4xl mx-auto">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">
            My Adoption Applications
          </h1>
          <p className="text-muted-foreground">
            Track the status of your adoption applications
          </p>
        </div>

        {applications.length === 0 ? (
          <Card>
            <CardContent className="py-12 text-center">
              <p className="text-muted-foreground">
                You haven't submitted any applications yet.
              </p>
            </CardContent>
          </Card>
        ) : (
          <div className="space-y-4">
            {applications.map((app) => (
              <Card
                key={app.id}
                className="overflow-hidden hover:shadow-lg transition-shadow"
              >
                <CardHeader className="pb-3">
                  <div className="flex justify-between items-start gap-4">
                    {/* Animal Image */}
                    {app.animal.imageUrl && (
                      <img
                        src={app.animal.imageUrl}
                        alt={app.animal.name}
                        className="w-24 h-24 object-cover rounded-lg flex-shrink-0"
                      />
                    )}

                    <div className="flex-1">
                      <h2 className="text-2xl font-bold">{app.animal.name}</h2>
                      <p className="text-sm text-muted-foreground">
                        {app.animal.breed?.name || app.animal.species.name} •{" "}
                        {app.animal.sex}
                      </p>

                      {/* Additional Animal Info */}
                      <div className="mt-2 space-y-1">
                        {app.animal.price > 0 && (
                          <p className="text-xs text-muted-foreground">
                            <span className="font-medium">Adoption Fee:</span> $
                            {app.animal.price}
                          </p>
                        )}
                      </div>
                    </div>

                    <Badge
                      variant={
                        app.status === "APPROVED"
                          ? "default"
                          : app.status === "REJECTED"
                          ? "destructive"
                          : "secondary"
                      }
                      className="capitalize flex-shrink-0"
                    >
                      {app.status}
                    </Badge>
                  </div>
                </CardHeader>

                <Separator />

                <CardContent className="pt-4">
                  <div className="space-y-3">
                    <div>
                      <p className="text-sm font-medium text-muted-foreground">
                        Application Date
                      </p>
                      <p className="text-base">
                        {new Date(app.applicationDate).toLocaleDateString()}
                      </p>
                    </div>

                    <div>
                      <p className="text-sm font-medium text-muted-foreground">
                        Your Message
                      </p>
                      <p className="text-base">{app.description}</p>
                    </div>

                    {app.reviewedByUserName && (
                      <div>
                        <p className="text-sm font-medium text-muted-foreground">
                          Reviewed By
                        </p>
                        <p className="text-base">{app.reviewedByUserName}</p>
                      </div>
                    )}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </MainLayout>
  );
}
