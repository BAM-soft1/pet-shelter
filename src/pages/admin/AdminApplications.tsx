import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function AdminApplications() {
  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold">Adoption Applications</h2>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Pending Applications</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-muted-foreground">Coming soon...</p>
        </CardContent>
      </Card>
    </div>
  );
}
