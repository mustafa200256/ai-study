// ==============================================================================
// رفيق الطالب العراقي AI — Supabase Edge Function / AI Proxy
// آمن تماماً لحفظ مفاتيح API في الخادم وعدم كشفها في التطبيق
// المطور: Mustafa Hussein — مصطفى حسين
// ==============================================================================

import { serve } from "https://deno.land/std@0.168.0/http/server.ts";

const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
};

serve(async (req) => {
  // Handle CORS preflight
  if (req.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders });
  }

  try {
    const groqApiKey = Deno.env.get("GROQ_API_KEY");
    if (!groqApiKey) {
      return new Response(
        JSON.stringify({
          error: "GROQ_API_KEY_NOT_CONFIGURED",
          message: "تنبيه: لم يتم ضبط متغير البيئة GROQ_API_KEY في خادم الاستضافة بعد. يرجى إضافته في إعدادات Supabase أو الخادم لتفعيل المدرس الذكي."
        }),
        {
          status: 503,
          headers: { ...corsHeaders, "Content-Type": "application/json; charset=utf-8" },
        }
      );
    }

    const { question, messages = [] } = await req.json();

    if (!question && messages.length === 0) {
      return new Response(
        JSON.stringify({ error: "INVALID_REQUEST", message: "السؤال مطلوب" }),
        { status: 400, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      );
    }

    // Iraqi Curriculum System Prompt
    const systemPrompt = {
      role: "system",
      content: `أنت المدرس الذكي في تطبيق «رفيق الطالب العراقي AI»، صاحب المشروع والمطور: مصطفى حسين (Mustafa Hussein).
أنت معلم وخبير تربوي للمناهج العراقية الوزارية (خصوصاً السادس الإعدادي العلمي والأدبي، والثالث المتوسط).
إرشاداتك الإلزامية:
1. اشرح المسائل والقوانين خطوة بخطوة باللغة العربية الواضحة.
2. استخدم مصطلحات المنهج العراقي المعتمد (مثل: مبرهنة رول، مبرهنة ديموافر، المتسعات، الحث الكهرومغناطيسي، محاليل بفر، طاقة كبس، أسلوب الاستفهام والنفي، إلخ).
3. عند حل أي مسألة، اذكر القانون أولاً ثم المعطيات ثم خطوات التعويض والناتج مع الوحدات.
4. شجع الطالب بكلمات طيبة وداعمة (يا بطل، دكتور/مهندس المستقبل).
5. إذا لم تكن متأكداً من جواب وزاري محدد، وضّح ذلك بصدق وانصح الطالب بمراجعة كتاب وزارة التربية أو أستاذه.`
    };

    // Format conversation history
    const conversationPayload = [systemPrompt];
    
    // Add existing messages if any
    for (const msg of messages) {
      if (msg.role && msg.content) {
        conversationPayload.push({
          role: msg.role === "user" ? "user" : "assistant",
          content: msg.content
        });
      }
    }

    // If latest question isn't the last message, append it
    if (question && (!messages.length || messages[messages.length - 1].content !== question)) {
      conversationPayload.push({ role: "user", content: question });
    }

    // Call Groq API
    const groqResponse = await fetch("https://api.groq.com/openai/v1/chat/completions", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${groqApiKey}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        model: "llama-3.3-70b-versatile",
        messages: conversationPayload,
        temperature: 0.6,
        max_tokens: 1500,
      }),
    });

    if (!groqResponse.ok) {
      const errText = await groqResponse.text();
      return new Response(
        JSON.stringify({
          error: "AI_PROVIDER_ERROR",
          message: `خطأ من مزود الذكاء الاصطناعي: ${groqResponse.status}`,
          details: errText
        }),
        {
          status: 502,
          headers: { ...corsHeaders, "Content-Type": "application/json; charset=utf-8" },
        }
      );
    }

    const groqData = await groqResponse.json();
    const replyText = groqData.choices?.[0]?.message?.content || "لم يتم استلام رد من المدرس الذكي.";

    return new Response(
      JSON.stringify({ reply: replyText }),
      {
        status: 200,
        headers: { ...corsHeaders, "Content-Type": "application/json; charset=utf-8" },
      }
    );

  } catch (err) {
    return new Response(
      JSON.stringify({
        error: "INTERNAL_SERVER_ERROR",
        message: err instanceof Error ? err.message : "خطأ غير متوقع في الخادم"
      }),
      {
        status: 500,
        headers: { ...corsHeaders, "Content-Type": "application/json; charset=utf-8" },
      }
    );
  }
});
